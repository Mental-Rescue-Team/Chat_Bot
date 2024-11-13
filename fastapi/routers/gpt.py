from fastapi import APIRouter
from pydantic import BaseModel
from openai import OpenAI

router = APIRouter()


client = OpenAI(api_key="gpt-api-key")  

class InputData(BaseModel):
    text: str

@router.post("/gpt")
async def get_gpt_response(data: InputData):
        completion = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": data.text}
            ]
        )
        gpt_response = completion.choices[0].message.content.strip()
        return {"response": gpt_response}

