
import torch
from torch.utils.data import Dataset

class CustomDataset(Dataset):
    def __init__(self, data, tokenizer):
        IGNORE_INDEX = -100
        self.inp = []
        self.label = []

        PROMPT = '''당신은 유능한 AI 어시스턴트 입니다. 사용자의 질문에 대해 친절하게 답변해주세요.'''

        def formatting_func2(data):
            output_texts = []
            for message in data['messages_list']:
                content = message['content']
                role = message['role']

                if role == "user":
                    output_texts.append(f" user : {content}")
                elif role == "assistant":
                    output_texts.append(f" assistant : {content}")
                else:
                    output_texts.append(f" system : {content}")

            full_text = "\n".join(output_texts)

            output_lines = full_text.split("\n")
            label = output_lines[-1] if output_lines else ""
            output_texts = "\n".join(output_lines[:-1])

            return output_texts, label

        for example in data:
            
            output_texts, label = formatting_func2(example)
            message = [
                {"role": "system", "content": PROMPT},
                {"role": "user", "content": output_texts},
            ]
            
            source = tokenizer.apply_chat_template(
                message,
                add_generation_prompt=True,
                return_tensors="pt",
            )

            if label:
                label += tokenizer.eos_token
            target = tokenizer(
                label,
                return_attention_mask=False,
                add_special_tokens=False,
                return_tensors="pt"
            )
            target["input_ids"] = target["input_ids"].type(torch.int64)

            input_ids = torch.concat((source[0], target["input_ids"][0]))
            labels = torch.concat((torch.LongTensor([IGNORE_INDEX] * source[0].shape[0]), target["input_ids"][0]))
            self.inp.append(input_ids)
            self.label.append(labels)

    def __len__(self):
        return len(self.inp)

    def __getitem__(self, idx):
        return self.inp[idx]


class DataCollatorForSupervisedDataset(object):
    def __init__(self, tokenizer):
        self.tokenizer = tokenizer

    def __call__(self, instances):
        input_ids, labels = tuple([instance[key] for instance in instances] for key in ("input_ids", "labels"))
        input_ids = torch.nn.utils.rnn.pad_sequence(
            [torch.tensor(ids) for ids in input_ids], batch_first=True, padding_value=self.tokenizer.pad_token_id
        )
        labels = torch.nn.utils.rnn.pad_sequence([torch.tensor(lbls) for lbls in labels], batch_first=True, padding_value=-100)
        return dict(
            input_ids=input_ids,
            labels=labels,
            attention_mask=input_ids.ne(self.tokenizer.pad_token_id),
        )