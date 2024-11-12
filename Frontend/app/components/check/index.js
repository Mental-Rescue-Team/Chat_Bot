import React, {useState} from 'react';
import {StyleSheet, Text, View, FlatList, TouchableWithoutFeedback, Alert} from 'react-native';
import { RadioButton, Button } from 'react-native-paper';
import { questionSets } from './questions';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const CheckComponent = () =>  {

  const [selectedTestType, setSelectedTestType] = useState(1); // 기본 검사 종류 설정
  const [answers, setAnswers] = useState([]);

  const questions = questionSets[selectedTestType];

  // 응답을 처리
  const handleSelectOption = (questionId, option) => {
    setAnswers((prevAnswers) => ({
      ...prevAnswers,
      [questionId]: option,
    }));
  };


  // 점수 계산 함수
  const calculateScore = () => {
    let totalScore = 0;
    questions.forEach((q) => {
      totalScore += q.scoreMap[answers[q.id]] || 0;
    });
    return totalScore;
  };

  // 결과 서버로 전송
  const submitResults = async () => {
    const score = calculateScore();

    try {

      const tokenData = await AsyncStorage.getItem('Tokens');
      const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
      const accessToken = parsedTokenData?.accessToken;

      if (!accessToken) {
        console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
        alert("로그인이 필요합니다.");
        return;
      }

      console.log("보낼 데이터:", score);
      console.log("Authorization 헤더:", `Bearer ${accessToken}`);

      const response = await axios.post(`http://ceprj.gachon.ac.kr:60016/diagnose/${selectedTestType}`, 
        score,
      {
        headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
        }
      }
    );
      console.log('서버 응답:', response.data);
      // 서버 응답에 따라 알림 표시
      Alert.alert('검사 결과', `${response.data}`, [
        { text: '확인' },
      ]);
    } catch (error) {
      if (error.response) {
        // 서버에서 응답이 왔을 경우 (status 코드 포함)
        console.error('서버 응답 오류:', error.response.data);
        Alert.alert('서버 오류', `서버 오류 발생: ${error.response.status} - ${error.response.data}`);
      } else if (error.request) {
        // 요청이 보내졌지만 응답을 받지 못했을 경우
        console.error('요청 오류:', error.request);
        Alert.alert('요청 오류', '서버로부터 응답을 받지 못했습니다.');
      } else {
        // 기타 오류
        console.error('알 수 없는 오류:', error.message);
        Alert.alert('알 수 없는 오류', '오류가 발생했습니다. 다시 시도해 주세요.');
      }
    }
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
        <View style={styles.testSelector}>
          <Button
          mode="outlined"
          style={styles.selectButton}
          labelStyle={styles.selectButText}
          onPress={() => setSelectedTestType(1)} >
          우울증(PHQ-9)</Button>
          <Button
          mode="outlined"
          style={styles.selectButton}
          labelStyle={styles.selectButText} 
          onPress={() => setSelectedTestType(2)} >
          불안(PSWQ-CK)</Button>
          <Button
          mode="outlined"
          style={styles.selectButton}
          labelStyle={styles.selectButText} 
          onPress={() => setSelectedTestType(3)}>
          자존감(RSES)</Button>
          <Button
          mode="outlined"
          style={styles.selectButton}
          labelStyle={styles.selectButText}
          onPress={() => setSelectedTestType(4)}>
          불면증(ISI-K)</Button>
        </View>
        <FlatList
        style={{width: '90%'}}
          data={questions}
          renderItem={renderItem}
          keyExtractor={(item) => item.id}
          contentContainerStyle={styles.flatListContainer}
        />
        <TouchableWithoutFeedback onPress={submitResults}>
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
  testSelector: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 20,
    width: '100%'
  },
  selectButton: {
    width: '45%', // 버튼을 2x2로 배치
    margin: 5,
    borderColor: '#F2F2F2',
    borderWidth: 1
  },
  flatListContainer: {
    paddingBottom: 10,
  },
  questionContainer: {
    marginBottom: 10,
    width: '100%',
  },
  question: {
    fontSize: 16,
    marginBottom: 5,
    color: 'black',
    fontWeight: 'bold'
  },
  radioButton: {
    backgroundColor: 'transparent',
  },
  radioLabel: {
    fontSize: 14,
  },
  button: {
    backgroundColor: '#7A5ADB',
    paddingVertical: 15,
    paddingHorizontal: 30,
    borderRadius: 10,
    width: '100%',
    alignItems: 'center',
  },
  buttext: {
    color: 'white',
    fontSize: 15,
    fontWeight: 'bold',
  },
  selectButText: {
    fontSize: 16,
  },

});

export default CheckComponent;