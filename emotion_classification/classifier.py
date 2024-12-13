import gluonnlp as nlp
import torch
from torch import nn
import torch.nn.functional as F
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader
import numpy as np
import pandas as pd
from tqdm.notebook import tqdm
from kobert_tokenizer import KoBERTTokenizer
from transformers import BertModel
from transformers import AdamW
from transformers.optimization import get_cosine_schedule_with_warmup

from torch.utils.data import Dataset
import warnings
warnings.filterwarnings('ignore', category=FutureWarning)

# print("hello")
# Load the tokenizer for KoBERT
tokenizer = KoBERTTokenizer.from_pretrained('skt/kobert-base-v1')

# Load the KoBERT model and vocab
def get_kobert_model(model_path, vocab_file, ctx="cpu"):
    bertmodel = BertModel.from_pretrained(model_path)
    device = torch.device(ctx)
    bertmodel.to(device)
    bertmodel.eval()
    vocab_b_obj = nlp.vocab.BERTVocab.from_sentencepiece(vocab_file, padding_token='[PAD]')
    return bertmodel, vocab_b_obj

class BERTSentenceTransform:
    def __init__(self, tokenizer, max_seq_length, pad=True, pair=True):
        self._tokenizer = tokenizer
        self._max_seq_length = max_seq_length
        self._pad = pad
        self._pair = pair

    def __call__(self, line):
        text_a = line[0]
        tokens_a = self._tokenizer.tokenize(text_a)

        # 최대 길이를 넘지 않도록 자르기
        if len(tokens_a) > self._max_seq_length - 2:
            tokens_a = tokens_a[:self._max_seq_length - 2]

        # [CLS] + tokens_a + [SEP]
        tokens = [self._tokenizer.cls_token] + tokens_a + [self._tokenizer.sep_token]
        segment_ids = [0] * len(tokens)

        input_ids = self._tokenizer.convert_tokens_to_ids(tokens)
        valid_length = len(input_ids)

        if self._pad:
            padding_length = self._max_seq_length - valid_length
            input_ids += [self._tokenizer.pad_token_id] * padding_length
            segment_ids += [0] * padding_length

        return np.array(input_ids, dtype='int32'), np.array(valid_length, dtype='int32'), np.array(segment_ids, dtype='int32')


class BERTClassifier(torch.nn.Module):
    def __init__(self,
                 bert,
                 hidden_size=768,
                 num_classes=5,  # 감정 클래스 개수
                 dr_rate=None):
        super(BERTClassifier, self).__init__()
        self.bert = bert
        self.dr_rate = dr_rate
        self.classifier = torch.nn.Linear(hidden_size, num_classes)
        if dr_rate:
            self.dropout = torch.nn.Dropout(p=dr_rate)

    def gen_attention_mask(self, token_ids, valid_length):
        attention_mask = torch.zeros_like(token_ids)
        for i, v in enumerate(valid_length):
            attention_mask[i][:v] = 1
        return attention_mask.float()

    def forward(self, token_ids, valid_length, segment_ids):
        attention_mask = self.gen_attention_mask(token_ids, valid_length)
        _, pooler = self.bert(input_ids=token_ids,
                              token_type_ids=segment_ids.long(),
                              attention_mask=attention_mask.float().to(token_ids.device),
                              return_dict=False)
        if self.dr_rate:
            out = self.dropout(pooler)
        return self.classifier(out)


# 예측 함수
def predict_emotion(sentence, model_path, tokenizer, vocab, max_len=100, device='cpu'):

    # Load the trained model
    bertmodel, vocab = get_kobert_model('skt/kobert-base-v1', tokenizer.vocab_file)
    model = BERTClassifier(bertmodel, dr_rate=0.5).to(device)

    # Load the model weights
    model.load_state_dict(torch.load(model_path, map_location=device, weights_only=True), strict=False)

    model.eval()

    # Mapping from numerical label to emotion string
    reverse_emotion_mapping = { 0 : '기쁨', 1 : '분노', 2 : '불안', 3 : '슬픔', 4 : '평온'}

    # Preprocess the input sentence
    transform = BERTSentenceTransform(tokenizer, max_seq_length=max_len, pad=True, pair=False)
    input_ids, valid_length, segment_ids = transform([sentence])

    # Convert numpy arrays to tensors
    input_ids = torch.tensor(np.array([input_ids]), dtype=torch.long).to(device)
    segment_ids = torch.tensor(np.array([segment_ids]), dtype=torch.long).to(device)
    valid_length = torch.tensor(np.array([valid_length]), dtype=torch.int32).to(device)

    # Set the model to evaluation mode
    model.eval()

    with torch.no_grad():
        # Get the output from the model
        output = model(input_ids, valid_length, segment_ids)
        # Get the predicted class (label) with the highest score
        predicted_label = torch.argmax(output, dim=1).cpu().numpy()[0]

    # Return the corresponding emotion label
    prediction = reverse_emotion_mapping[predicted_label]

    return prediction
