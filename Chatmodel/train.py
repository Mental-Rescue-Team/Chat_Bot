import argparse
import os
import torch
from transformers import (AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig, TrainingArguments)
from trl import SFTTrainer
from peft import prepare_model_for_kbit_training, LoraConfig, get_peft_model
import wandb
from huggingface_hub import login
from datasets import Dataset, load_dataset
from src.data import CustomDataset, DataCollatorForSupervisedDataset

os.environ["TOKENIZERS_PARALLELISM"] = "false"

parser = argparse.ArgumentParser()

g = parser.add_argument_group("Common Parameter")
g.add_argument("--model_id", type=str, required=True, help="model file path")
g.add_argument("--batch_size", type=int, default=1, help="batch size")
g.add_argument("--learning_rate", type=float, default=2e-4, help="learning rate")
g.add_argument("--epochs", type=int, default=1, help="epochs")
g.add_argument("--save_dir", type=str, help="save directory")


def main(args):
    quantization_config = BitsAndBytesConfig(
        load_in_4bit=True,
        bnb_4bit_compute_dtype=torch.bfloat16,
        bnb_4bit_use_double_quant=True,
        bnb_4bit_quant_type='nf4'
    )

    model = AutoModelForCausalLM.from_pretrained(
        args.model_id,
        torch_dtype=torch.bfloat16,  
        device_map="auto",
        quantization_config=quantization_config,
    )

    model = prepare_model_for_kbit_training(model)

    lora_config = LoraConfig(
        r=8,
        target_modules=[
            "q_proj", "k_proj", "v_proj", "o_proj", "gate_proj", "up_proj", "down_proj",
        ],
        lora_alpha=32,
        lora_dropout=0.05
    )
    model = get_peft_model(model, lora_config)

    model.config.use_cache = False
    model.gradient_checkpointing_enable()

    tokenizer = AutoTokenizer.from_pretrained(args.model_id)
    
    tokenizer.pad_token = tokenizer.eos_token
    
    login(token='huggingface_token')

    train_dataset = load_dataset("yoyomo/modataset-ko", split="train")
    
    train_dataset = CustomDataset(train_dataset, tokenizer)
    
    train_dataset = Dataset.from_dict({
        'input_ids': train_dataset.inp,
        "labels": train_dataset.label,
        })
    
    data_collator = DataCollatorForSupervisedDataset(tokenizer=tokenizer)
    
    training_arguments = TrainingArguments(  
        output_dir="exaone_mental_pretrained",
        push_to_hub=True,
        do_train=True,
        num_train_epochs=args.epochs,
        per_device_train_batch_size=args.batch_size,
        gradient_accumulation_steps=8,
        optim="adamw_bnb_8bit",
        save_steps=100,
        logging_steps=10,
        lr_scheduler_type="cosine",
        learning_rate=args.learning_rate,
        weight_decay=0.001,
        max_grad_norm=0.3,
        fp16=True,
        bf16=False,
        max_steps=-1,
        warmup_steps=20,
        save_total_limit=1,
    )

    trainer = SFTTrainer(
        model=model,
        tokenizer=tokenizer,
        train_dataset=train_dataset,
        max_seq_length=1024,
        args=training_arguments,
        data_collator=data_collator,
    )

    trainer.train()

    trainer.push_to_hub()


if __name__ == "__main__":
    args = parser.parse_args()
    wandb.init(project='jolproject')
    
    wandb.run.name = args.model_id
    wandb.run.save()

    wandb_args = {
        "learning_rate": args.learning_rate,
        "epochs": args.epochs,
        "batch_size": args.batch_size
    }
    wandb.config.update(wandb_args)
    exit(main(args))
