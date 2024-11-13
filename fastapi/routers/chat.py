from fastapi import APIRouter
from pydantic import BaseModel
from transformers import AutoModelForCausalLM, AutoTokenizer, BitsAndBytesConfig
import re

router = APIRouter()

model_name = "yoyomo/exaone_mental_pretrained"
bnb_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_quant_type="nf4",
    bnb_4bit_compute_dtype="float16",
    bnb_4bit_use_double_quant=False,
)
model = AutoModelForCausalLM.from_pretrained(model_name, quantization_config=bnb_config, trust_remote_code=True)
tokenizer = AutoTokenizer.from_pretrained(model_name)

conv = [{
    "content": "[|system|] 당신은 유능한 AI 어시스턴트 입니다. 사용자의 질문에 대해 친절하게 답변해주세요. \n  system : 당신은 도움이 되고 기쁨이 넘치는 정신 치료 보조입니다. 가능한 한 도와주고 기쁘게 답변하되, 안전을 유지해주세요. 여러분의 답변은 해로운, 비윤리적인, 인종 차별적인, 성 차별적인, 유해한, 위험한 또는 불법적인 내용을 포함해서는 안되며 사회적으로 편향되지 않고 긍정적이어야 합니다. [|endofturn|]", 
    "role": "system"
}]

class ChatRequest(BaseModel):
    user_input: str

def generate_response(conv, model, tokenizer):
    prompt = tokenizer.apply_chat_template(conv, tokenize=False)  
    inputs = tokenizer(prompt, return_tensors="pt")  
    input_ids = inputs["input_ids"]
    output_ids = model.generate(
        **inputs,
        max_length=1024,
        do_sample=True,
        top_p=0.95,
        top_k=50,
        temperature=1
    )
    output_text = tokenizer.decode(output_ids[0][input_ids.shape[1]:], skip_special_tokens=True) 
    return output_text

@router.post("/chat")
def chat_with_model(request: ChatRequest):
    user_input = request.user_input
    
    conv.append({"content": user_input, "role": "user"}) 

    response = generate_response(conv, model, tokenizer) 
    conv.append({"content": response, "role": "assistant"})
    
    yoyo = response.replace("\\", "").strip()

    match = re.search(r":\s*(.*)", yoyo)
    
    if match:
        yoyo = match.group(1).strip()
    else:
        yoyo = response.strip()

    return yoyo
