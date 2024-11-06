#Memo

# @app.post("/predict")
# async def predict(data: InputData):
#     print(f"Received text: '{data.text}'")  # 입력 데이터 로그
#     result = len(data.text) * 2  # 문자열 길이의 두 배 계산
#     print(result)
#     return {"result": result}

#*가상환경 활성화 코드 :  .\venv\Scripts\activate
#*서버의 위치   : C:\Users\류성열\Desktop\CB0928\fastapi_server
#*fastapi server 실행 코드 : uvicorn Server:app --reload
#현재 인터프리터 정보 
#이름: python 3.12.0('venv')
#경로 :~\Desktop\CB0928\fastapi_server\venv\Scripts\python.exe

# 파이썬 버전 : Python 3.12.0
# 가상환경 내 파이썬 버전 : Python 3.12.0

#10.13일 부터 생긴 트러블
#문제 상황 : 왠지 모르게 uvicorn Server:app --reload이 안된다
#해결 방법 : python -m uvicorn Server:app --reload으로 진행 
#우선 환경변수중 하나는 python 311로 되어 있음-> 문제가 되는가?



