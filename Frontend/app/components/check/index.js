import React, {useState} from 'react';
import {StyleSheet, Text, View, Button, FlatList, ScrollView, TouchableWithoutFeedback} from 'react-native';
import { RadioButton } from 'react-native-paper';

const CheckComponent = () =>  {
  // 진단검사 질문과 선택지
  const questions = [
    { id: '1', question: "오늘 기분이 어떠신가요?", options: ["좋음", "보통", "나쁨"] },
    { id: '2', question: "최근 스트레스가 많으셨나요?", options: ["예", "아니오"] },
    { id: '3', question: "최근 스트레스가 많으셨나요?", options: ["예", "아니오"] },
    { id: '4', question: "최근 스트레스가 많으셨나요?", options: ["예", "아니오"] },
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