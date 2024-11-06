import React, {useState, useEffect} from 'react';
import {StyleSheet, Text, TextInput, View, Image} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Sunny from '../../assets/images/sunny.jpg';
import Normal from '../../assets/images/normal.jpg';
import Sad from '../../assets/images/sad.jpg';
import Rain from '../../assets/images/rain.jpg';
import Angry from '../../assets/images/angry.jpg';

const WeatherTab = () =>  {

  const [message, setMessage] = useState('오늘의 기분을 알려주세요.');
  const [image, setImage] = useState(Sunny); // 기본 이미지 설정

  useEffect(() => {
    const loadEmotionData = async () => {
      try {
        const value = await AsyncStorage.getItem('DiaryData');
        if (value !== null) {
          const parsedValue = JSON.parse(value);
          const emotion = parsedValue.emotion;

          // 감정에 따라 메시지와 이미지를 설정
          switch (emotion) {
            case '기쁨':
              setMessage('오늘은 정말 기쁜 날이군요!');
              setImage(Sunny);
              break;
            case '평온':
              setMessage('평온한 하루를 보내고 있군요.');
              setImage(Normal);
              break;
            case '슬픔':
              setMessage('조금 슬픈 하루인 것 같아요.');
              setImage(Sad);
              break;
            case '분노':
              setMessage('화가 나는 하루인가요? 차분해지세요.');
              setImage(Angry);
              break;
            case '불안':
              setMessage('오늘은 불안하군요.');
              setImage(Rain);
              break;
            default:
              setMessage('오늘의 기분을 알려주세요.');
              setImage(Sunny); // 기본 이미지 설정
              break;
          }
        } else {
          console.log('No DiaryData found in AsyncStorage');
        }
      } catch (e) {
        console.error('Error loading emotion data:', e);
      }
    };

    loadEmotionData();
  }, []);

  return (
    <View style={{flex: 1, flexDirection: 'row', backgroundColor: 'white'}}>
      <View style={styles.container}>
        <Text style={styles.mainText}>Today's My Weather</Text>
        <Text style={styles.subText}>{message}</Text>
      </View>
      <View style={styles.image}>
        <Image
            source={image}
            resizeMode={'contain'}
            style={{
                width: 150
            }}
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