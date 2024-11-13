from fastapi import APIRouter
from pydantic import BaseModel
from classifier import predict_emotion, KoBERTTokenizer

router = APIRouter()

class EmotionRequest(BaseModel):
    user_input: str

def emotion_response(user_input):
    model_path = "final_emotion_model.pth"
    tokenizer = KoBERTTokenizer.from_pretrained('skt/kobert-base-v1')
    vocab_file = tokenizer.vocab_file
    predicted_emotion = predict_emotion(user_input, model_path, tokenizer, vocab_file)
    return predicted_emotion

@router.post("/emotion")
def emotion(data: EmotionRequest):
    user_input = data.user_input
    print(user_input)
    response = emotion_response(user_input)
    print(response)
    return response
