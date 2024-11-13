import uvicorn
from fastapi import FastAPI
from routers import chat, gpt, emotion

app = FastAPI()

app.include_router(chat.router, prefix="/api")
app.include_router(gpt.router, prefix="/api")
app.include_router(emotion.router, prefix="/api")

@app.get("/")
async def read_root():
    return {
        "message": "1. Emotion Classification Model <br> 2. AI ChatBot Model <br> 3. GPT-4.o Model"
    }

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8006, reload=True)
