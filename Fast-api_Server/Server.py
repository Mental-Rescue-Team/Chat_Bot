from fastapi import FastAPI
from pydantic import BaseModel
import uvicorn
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from openai import OpenAI

#Version : 1.51.2
#print(openai.__version__)

#FastAPI 앱을 생성하는 코드
app = FastAPI()

# CORS 미들웨어 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"], 
    allow_credentials=True,
    allow_methods=["*"],  # 모든 HTTP 메서드 허용
    allow_headers=["*"],  # 모든 헤더 허용
)

# 입력 데이터를 받기 위한 모델 정의
class InputData(BaseModel):
    text: str

# 기본 라우팅
@app.get("/")
async def read_root():
    return {"message": "1. Emotion Classification Model <br> 2.AI ChatBot Model <br> 3. GPT-4.o Model"}


#GPT-Test Start
client = OpenAI(
    api_key="your gpt api key"
)

completion = client.chat.completions.create(
  model = "gpt-3.5-turbo",
  messages = [
    {"role": "system", "content": "You are a helpful assistant."},
    {"role": "user", "content": "안녕 너는 지금 잘 작동하고 있니?너는 버전 몇이니?"},
  ]
)
#GPT-Test Finish

# GPT-4에 메시지를 보내기 위한 POST 라우팅
@app.post("/gpt")
async def get_gpt_response(data: InputData):
    
    try:
        # OpenAI API 클라이언트 생성
        client = OpenAI(api_key="your gpt api key")

        # GPT-3.5에게 메시지 전송
        completion = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": data.text}  # Spring 서버에서 받은 text 데이터 사용
            ]   
        )

        # GPT-4의 응답 내용
        gpt_response = completion.choices[0].message.content.strip()  # 응답 내용 정리
        print(gpt_response)
        return {gpt_response}  # Spring 서버에 응답 반환

    except Exception as e:
        return {"error": str(e)}  # 에러 발생 시 에러 메시지 반환

print(completion.choices[0].message.content.strip())



if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)







