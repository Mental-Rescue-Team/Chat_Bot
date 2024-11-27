import React, { useState, useEffect } from 'react';
import {StyleSheet, Text, View, TextInput, ScrollView, Image} from 'react-native';
import DateHead from '../diary/dateHead';
import axios from 'axios';
import AsyncStorage from "@react-native-async-storage/async-storage";
import { useIsFocused } from '@react-navigation/core';

const DiaryView = ({route}) =>  {

  const { selectedDate } = route.params || {};
  const [text, setText] = useState('');
  const [image, setImage] = useState('');
  const [loading, setLoading] = useState(false);
  const [hasDiary, setHasDiary] = useState(true); // 일기 여부 상태 추가

  const isFocused = useIsFocused();
  const displayDate = selectedDate ? new Date(selectedDate) : new Date();

  useEffect(() => {
    if (isFocused) {
      const fetchData = async () => {
        setLoading(true);
        try {
          const tokenData = await AsyncStorage.getItem('Tokens');
          const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
          const accessToken = parsedTokenData?.accessToken;

          if (!accessToken) {
            console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
            alert("로그인이 필요합니다.");
            return;
          }

          console.log("보낼 데이터:", { selectedDate });
          console.log("Authorization 헤더:", `Bearer ${accessToken}`);
          // 서버에 날짜를 기반으로 GET 요청을 보내고 데이터 확인
          const response = await axios.get('http://ceprj.gachon.ac.kr:60016/diary', {
            headers: {
              Authorization: `Bearer ${accessToken}`, // 필요시 accessToken 추가
            },
            params: {
              date: selectedDate, // selectedDate를 쿼리 파라미터로 전달
            },
          });

          console.log('서버에서 받은 데이터:', response.data); // 받은 데이터 콘솔에 출력
          if (!response.data || !response.data.diaryText) {
            setHasDiary(false); // 일기 데이터가 없으면 '작성한 일기가 없습니다' 텍스트 표시
          } else {
            setHasDiary(true);
            setText(response.data.diaryText); // 서버에서 받은 일기 텍스트로 업데이트
            setImage(response.data.comicURL); // 서버에서 받은 이미지 URL로 업데이트
          }

        } catch (error) {
          // console.error('서버 요청 오류:', error);
          alert("작성된 일기가 없습니다");
          setHasDiary(false)
        } finally {
          setLoading(false);
        }
      };

      if (selectedDate) {
        fetchData();
      }
    }
  }, [isFocused, selectedDate]); // selectedDate가 변경될 때마다 호출

  const onChangeText = (inputText) => {
    setText(inputText);
  };

  return (
    <View style={{flex: 1, backgroundColor: 'white', justifyContent: 'center'}}>
      <ScrollView>
        <View style={{justifyContent: 'flex-start', marginBottom: 10, marginTop: 15}}>
          <DateHead date={displayDate}/>
        </View>
        <View style={{justifyContent: 'center', alignItems: 'center',}}>
          {!hasDiary ? (
              <Text>작성한 일기가 없습니다</Text> // 일기가 없으면 해당 텍스트 표시
            ) : (
              <>
                <TextInput
                        onChangeText={onChangeText}
                        value={text}
                        placeholder="오늘 작성한 일기입니다"
                        placeholderTextColor={'grey'}
                        style={styles.input}
                        multiline
                        editable={false}
                  />
                  
                {image && image !== "" ? (
                  <Image source={{ uri: image }} resizeMode={'contain'} style={{ width: 350, height: 350 }} />
                ) : (
                  <Text>No image available</Text>
                )}
            </>
          )}
        </View>
      </ScrollView>  
    </View>
  );
};

const styles = StyleSheet.create({
  input: {
    width: '90%',
    // height: 350,
    minHeight: 200,
    maxHeight: 450, 
    fontSize: 16,
    borderColor: '#999',
    borderWidth: 1,
    borderRadius: 10,
    padding: 10,
    textAlignVertical: 'top',
    marginBottom: 20,
    color: 'black'
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
    fontSize: 15,
    fontWeight: 'bold',
  },
  mainText: {
    fontFamily: 'Paperlogy-7Bold',
    fontSize: 30,
    padding: 20,
    color: '#7A5ADB',
  },
});

export default DiaryView;