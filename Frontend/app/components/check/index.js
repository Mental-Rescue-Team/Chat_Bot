import React, {useState} from 'react';
import {StyleSheet, Text, View, FlatList, TouchableWithoutFeedback} from 'react-native';
import { RadioButton } from 'react-native-paper';

const CheckComponent = () =>  {
  // 진단검사 질문과 선택지
  const questions = [
    { 
      id: '1', 
      question: "기분이 가라앉거나, 우울하거나, 희망이 없다고 느꼈다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '2', 
      question: "평소 하던 일에 대한 흥미가 없어지거나 즐거움을 느끼지 못했다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '3', 
      question: "잠들기가 어렵거나 자주 깼다 / 혹은 너무 많이 잤다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '4', 
      question: "평소보다 식욕이 줄었다 / 혹은 평소보다 많이 먹었다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '5', 
      question: "다른 사람들이 눈치 챌 정도로 평소보다 말과 행동이 느려졌다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '6', 
      question: "피곤하고 기운이 없었다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '7', 
      question: "내가 잘못 했거나, 실패했다는 생각이 들었다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '8', 
      question: "신문을 읽거나 TV를 보는 것과 같은 일상적인 일에도 집중 할 수가 없었다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
    { 
      id: '9', 
      question: "차라리 죽는 것이 더 낫겠다고 생각했다 / 혹은 자해할 생각을 했다.", 
      options: ["없음", "2-6일", "7-12일", "거의 매일"] 
    },
  ];

  const [answers, setAnswers] = useState([]);

  // 응답을 처리
  const handleSelectOption = (questionId, option) => {
    setAnswers((prevAnswers) => ({
      ...prevAnswers,
      [questionId]: option,
    }));
  };

  const renderItem = ({ item }) => (
    <View style={styles.questionContainer}>
      <Text style={styles.question}>{item.question}</Text>
      <RadioButton.Group
        onValueChange={(value) => handleSelectOption(item.id, value)}
        value={answers[item.id]}
      >
        {item.options.map((option) => (
          <RadioButton.Item
            key={option}
            label={option}
            value={option}
            style={styles.radioButton}
            labelStyle={styles.radioLabel}
          />
        ))}
      </RadioButton.Group>
    </View>
  );

  return (
      <View style={styles.container}>
        <Text style={styles.title}>진단검사</Text>
        <FlatList
        style={{width: '90%'}}
          data={questions}
          renderItem={renderItem}
          keyExtractor={(item) => item.id}
          contentContainerStyle={styles.flatListContainer}
        />
        <TouchableWithoutFeedback onPress={() => alert("검사가 완료되었습니다.")}>
            <View style={styles.button}>
                <Text style={styles.buttext}>검사결과 확인</Text>
            </View>
        </TouchableWithoutFeedback>
      </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
    backgroundColor: 'white'
  },
  title: {
    fontSize: 30,
    marginBottom: 20,
    fontFamily: 'Paperlogy-7Bold'
  },
  flatListContainer: {
    paddingBottom: 20,
  },
  questionContainer: {
    marginBottom: 20,
    width: '100%',
  },
  question: {
    fontSize: 18,
    marginBottom: 10,
  },
  radioButton: {
    backgroundColor: 'transparent',
  },
  radioLabel: {
    fontSize: 16,
  },
  button: {
    backgroundColor: '#7A5ADB',
    paddingVertical: 15,
    paddingHorizontal: 30,
    borderRadius: 10,
    marginBottom: 20,
    width: '100%',
    alignItems: 'center',
  },
  buttext: {
    color: 'white',
    fontSize: 15,
    fontWeight: 'bold',
  },

});

export default CheckComponent;