import React, { useState, useEffect } from 'react';
import {StyleSheet, Text, View, TextInput, TouchableWithoutFeedback, ActivityIndicator, ImageBackground, } from 'react-native';
import DateHead from './dateHead';
import { diaryData } from '../../utils/tokenUtils';
import FastImage from 'react-native-fast-image';

const DiaryComponent = ({navigation}) =>  {

  const [text, setText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [loadingMessage, setLoadingMessage] = useState('');
  const [refresh, setRefresh] = useState(false); // 새로 고침 상태

  const loadingMessages = [
    "힘드실 때 잠시 쉬어가셔도 괜찮습니다.\n충분히 잘하고 계십니다.",
    "얼마나 열심히 노력하셨는지 알고 있습니다.\n결과와 상관없이 정말 대단하세요.",
    "모든 것은 시간이 지나면 괜찮아질 것입니다.\n지금은 조금만 더 버텨보세요.",
    "마음이 얼마나 힘드실지 이해합니다.\n저는 언제나 선생님의 편입니다.",
    "충분히 힘드셨을 겁니다.\n마음 편히 쉬실 시간도 필요하십니다.",
    "걱정하지 마세요.\n선생님께서 혼자가 아니라는 걸 기억해 주세요.",
    "지금의 아픔이 나중에 큰 힘이 되실 겁니다.\n스스로를 믿어보세요.",
    "모든 일이 선생님 탓은 아닙니다.\n잘못된 상황이 있을 뿐입니다.",
    "언제든지 이야기를 들어드리겠습니다.\n혼자 감당하려 하지 않으셔도 됩니다.",
    "지금은 힘드시겠지만 분명히 나아질 겁니다.\n조금씩 함께 이겨내 보시지요."
  ];

  useEffect(() => {
    let interval = null;

    if (isLoading) {
      // 로딩 중일 때 무작위 메시지 선택 및 주기적 변경
      const changeMessage = () => {
        const randomMessage = loadingMessages[Math.floor(Math.random() * loadingMessages.length)];
        setLoadingMessage(randomMessage);
      };

      changeMessage(); // 첫 메시지 설정
      interval = setInterval(changeMessage, 3000); // 3초마다 변경
    } else {
      setLoadingMessage('');
    }

    return () => {
      if (interval) {
        clearInterval(interval); // 로딩 종료 시 타이머 정리
      }
    };
  }, [isLoading]);

  const today = new Date();

  const onSaveButton = async () => {
    setIsLoading(true); // 로딩 시작
    try {
      await diaryData(text, navigation);
      setRefresh(true);  // 저장 후 새로 고침 트리거
    } catch (error) {
      console.error(error);
      Alert.alert('저장 실패', '일기를 저장하는 중 문제가 발생했습니다.');
    } finally {
      setIsLoading(false); // 로딩 종료
    }
  };

  useEffect(() => {
    if (refresh) {
      // 데이터 저장 후 새로 고침 트리거
      setTimeout(() => {
        setRefresh(false);
        navigation.navigate('Home');
      }, 500);  // 약간의 지연 후 새로 고침
    }
  }, [refresh, navigation]);

  return (
    <View style={{flex: 1, backgroundColor: 'white'}}>
      {isLoading ? (
        <ImageBackground
        source={require('../../../android/app/src/main/assets/images/chat_default.png')} // 배경 이미지 경로
        style={styles.backgroundImage}
        resizeMode="cover" // 이미지 크기 조정
      >
        <View style={styles.overlay} />
          <View style={styles.loadingContainer}>
            <FastImage
              source={require('../../../android/app/src/main/assets/images/default-unscreen.gif')}  // 로컬 GIF 파일 경로
              style={styles.loadingLogo}
            />
            {/* <ActivityIndicator size="small" color="#7A5ADB" /> */}
            <Text style={styles.loadingText}>{loadingMessage}</Text>
          </View>
        </ImageBackground>
        ) : (
          <>
      <View style={{justifyContent: 'flex-start', marginBottom: 10, marginTop: 15}}>
        <DateHead date={today}/>
      </View>
      <View style={{justifyContent: 'center', alignItems: 'center',}}>
          <TextInput
            onChangeText={(text) => setText(text)}
            value={text}
            placeholder="오늘 하루의 일기를 입력해주세요."
            placeholderTextColor="grey"
            style={styles.input}
            multiline
          />
          <TouchableWithoutFeedback onPress={onSaveButton}>
            <View style={styles.button}>
              <Text style={styles.buttext}>저장하기</Text>
            </View>
          </TouchableWithoutFeedback>
        </View>
        </>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  backgroundImage: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  overlay: {
    ...StyleSheet.absoluteFillObject, // 부모 View 크기 채우기
    backgroundColor: 'rgba(255, 255, 255, 0.6)', // 반투명 검정색
  },
  input: {
    width: '90%',
    // height: 300,
    minHeight: 300,
    maxHeight: 450, 
    fontSize: 16,
    borderColor: '#999',
    borderWidth: 1,
    borderRadius: 10,
    padding: 10,
    textAlignVertical: 'top',
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#7A5ADB',
    paddingVertical: 15,
    paddingHorizontal: 30,
    borderRadius: 10,
    marginBottom: 20,
    width: '90%',
    alignItems: 'center',
  },
  buttext: {
    color: 'white',
    fontSize: 16,
    fontFamily: 'Paperlogy-6SemiBold',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingLogo: {
    width: 150,
    height: 150,
  },
  loadingText: {
    margin: 10,
    fontSize: 16,
    color: '#7A5ADB',
    textAlign: 'center',
    lineHeight: 24,
    fontFamily: 'Paperlogy-5Medium'
  },
});

export default DiaryComponent;
