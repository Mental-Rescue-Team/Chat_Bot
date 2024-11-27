import React, { useState, useEffect, useCallback } from 'react';
import {StyleSheet, Text, View, Image} from 'react-native';
import { useFocusEffect } from '@react-navigation/native'; // useFocusEffect 추가
import FastImage from 'react-native-fast-image';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import Sunny from '../../../android/app/src/main/assets/images/sunny.gif';
import Normal from '../../../android/app/src/main/assets/images/normal.gif';
import Sad from '../../../android/app/src/main/assets/images/sad.gif';
import Rain from '../../../android/app/src/main/assets/images/rain.gif';
import Angry from '../../../android/app/src/main/assets/images/angry.gif';
import Default from '../../../android/app/src/main/assets/images/sample.gif';


const WeatherTab = () =>  {

  const [message, setMessage] = useState('오늘의 기분을 알려주세요.');
  const [image, setImage] = useState(Default); // 기본 이미지 설정


    const fetchWeatherData = async () => {
      try {
        const tokenData = await AsyncStorage.getItem('Tokens');
        const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
        const accessToken = parsedTokenData?.accessToken;

        if (!accessToken) {
          console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
          alert('로그인이 필요합니다.');
          return;
        }

        // 서버에 GET 요청을 보내고 데이터 받기
        const response = await axios.get('http://ceprj.gachon.ac.kr:60016/diary/today/weather', {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        console.log('서버에서 받은 데이터:', response.data); // 받은 데이터 콘솔에 출력

        const weather = response.data; // 날씨 데이터 받아오기
        switch (weather) {
          case 'Sunny':
            setMessage('오늘 하루는 많이 행복해 보이시네요. 앞으로도 이런 날이 계속되길 응원할게요!');
            setImage(Sunny);
            break;
          case 'Cloudy':
            setMessage('평온한 하루를 보내고 있군요. 더 좋아질 수도 있지만 때로는 이런 평온함도 나쁘진 않다고 생각해요.');
            setImage(Normal);
            break;
          case 'Rainy':
            setMessage('오늘은 마음 속에 비가 내리네요. 울고 싶다면 언제든 울어도 괜찮아요. 필요한 일이 있으시다면 언제든 avery를 불러주세요.');
            setImage(Rain);
            break;
          case 'Stormy':
            setMessage('오늘은 화나는 일이 있으셨군요? 오늘 하루 스트레스가 많이 쌓였다면 그걸 풀어내는 것도 중요해요. 당신이 원하는 자유로운 방식으로 스트레스를 풀어보세요.');
            setImage(Angry);
            break;
          case 'Windy':
            setMessage('당장은 불안하시겠지만 때로는 너무 앞선 걱정은 쓸데없는 걱정거리인 경우가 많아요. 앞으로의 일은 아무도 모르는 거니까요.');
            setImage(Sad);
            break;
          default:
            setMessage('오늘의 기분을 알려주세요.');
            setImage(Default); // 기본 이미지 설정
            break;
        }
      } catch (error) {
        // console.error('서버 요청 오류:', error);
      }
    };

    useFocusEffect(
      useCallback(() => {
        fetchWeatherData();
      }, [])
    );

  return (
    <View style={{flex: 1, flexDirection: 'row', backgroundColor: 'white'}}>
      <View style={styles.container}>
        <Text style={styles.mainText}>Today's My Weather</Text>
        <Text style={styles.subText}>{message}</Text>
      </View>
      <View style={styles.image}>
        <FastImage
          source={image}
          style={{ width: 150, height: 150 }}
          resizeMode={FastImage.resizeMode.contain}
        />      
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 2.5,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20
  },
  input: {
    width: 250,
    height: 100,
    borderColor: '#999',
    borderWidth: 1,
    borderRadius: 10,
    padding: 10,
    textAlignVertical: 'top',
    marginTop: 20,
    fontFamily: 'Paperlogy-7Bold',
  },
  image: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20
  },
  mainText: {
    fontFamily: 'Paperlogy-7Bold',
    fontSize: 20,
    color: '#7A5ADB',
  },
  subText: {
    fontFamily: 'Paperlogy-7Bold',
    fontSize: 15,
    color: 'black',
    padding: 10,
  },
});

export default WeatherTab;