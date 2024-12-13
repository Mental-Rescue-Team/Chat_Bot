from classifier import predict_emotion, KoBERTTokenizer
import torch


model_path = "/home/t24201/seok/final_emotion_model.pth"


device = 'cuda' if torch.cuda.is_available() else 'cpu'


tokenizer = KoBERTTokenizer.from_pretrained('skt/kobert-base-v1')


vocab_file = tokenizer.vocab_file


input_sentence = "오늘 정말 화가 난다. 아침부터 일이 꼬이더니 결국 폭발하고 말았다. 회사에서 프로젝트에 대해 여러 번 설명했는데, 팀원들은 여전히 내 말을 무시하고 제멋대로 행동했다. 그동안 참았던 불만들이 터져 나왔다. 매번 내가 대신 처리해야 하고, 문제가 생기면 다 내 책임으로 돌아온다. 도대체 왜 내가 항상 이런 상황을 겪어야 하는지 모르겠다. 말해봤자 달라지는 것도 없고, 그저 나만 더 스트레스를 받는 것 같다. 더는 참을 수 없을 것 같다. 정말 화가 나서 폭발하기 직전이다. "
# input_sentence = "오늘은 모든 게 무너지는 기분이었다. 아무리 노력해도 내 마음을 이해해주는 사람은 없는 것 같다."
predicted_emotion = predict_emotion(input_sentence, model_path, tokenizer, vocab_file, device=device)


print(f"Predicted Emotion: {predicted_emotion}") 

