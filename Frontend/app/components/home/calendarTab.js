import React, { useState, useEffect, useCallback } from 'react';
import { format } from "date-fns";
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import { Calendar } from 'react-native-calendars';
import { useFocusEffect } from '@react-navigation/native'; // useFocusEffect 추가
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const URL = 'http://ceprj.gachon.ac.kr:60016'

function CalendarTab({navigation}) {

  const [selectedDate, setSelectedDate] = useState(
    format(new Date(), "yyyy-MM-dd"),
  );
  const [emotionData, setEmotionData] = useState({});
  const [lastClickedDate, setLastClickedDate] = useState(null);

  const emotionToEmoji = {
    "Sunny": "🌞",    // 기쁨
    "Rainy": "🌧️",   // 슬픔
    "Stormy": "🌩️",  // 분노
    "Cloudy": "☁️",   // 평온
    "Windy": "🌬️",   // 불안
  };

 
    const fetchData = async () => {
      try {
        
        const currentMonth = format(new Date(), "MM");
        const tokenData = await AsyncStorage.getItem('Tokens');
        const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
        const accessToken = parsedTokenData?.accessToken;


        console.log("보낼 데이터:", { currentMonth });
        console.log("Authorization 헤더:", `Bearer ${accessToken}`);

        // **서버에서 데이터 요청**
        const response = await axios.get(`${URL}/diary/every/weathers`, {
          headers: {
            Authorization: `Bearer ${accessToken}`  // accessToken을 Authorization 헤더에 포함
          },
          params: {
            month: currentMonth, // 예: "2024-11-07" 형식으로 날짜 전달
          },
    
        });
        
        console.log('서버 응답 데이터:', response.data);

        const data = response.data;  // 서버로부터 받은 데이터
        // **AsyncStorage에 저장**
        await AsyncStorage.setItem('emotionData', JSON.stringify(data));

        // **emotionData 상태 설정** (각 날짜에 맞는 이모지 저장)
        const emotionMap = data.reduce((acc, entry) => {
          const emoji = emotionToEmoji[entry.weather] || "❓";  // 변환된 이모지 또는 기본값
          acc[entry.diaryDate] = { emoji };
          return acc;
        }, {});
        setEmotionData(emotionMap);

      } catch (error) {
        console.error('데이터를 불러오는 중 오류 발생:', error);
      }
    };

    useFocusEffect(
      useCallback(() => {
        fetchData();
      }, [])
    );


  const handleDayPress = (day) => {
    if (lastClickedDate === day.dateString) {
      navigation.navigate('DiaryView', { selectedDate: day.dateString }); 
    } else {
      setLastClickedDate(day.dateString); 
      setSelectedDate(day.dateString);
    }
  };

  const getEmojiForDate = (date) => {
    return emotionData[date]?.emoji; 
  };

  

  return (
    <View style={{flex: 2}}>
        <Calendar 
        markedDates={{
          [selectedDate]: {
            selected: true,
            emoji: getEmojiForDate(selectedDate),
          },
          ...Object.keys(emotionData).reduce((acc, date) => {
            acc[date] = {
              emoji: emotionData[date]?.emoji,
            };
            return acc;
          }, {}),
        }}
        theme={{
          textMonthFontSize: 20,
          textMonthFontFamily: 'Paperlogy-7Bold',
          textDayFontFamily: 'Paperlogy-5Medium',
          textDayHeaderFontFamily: 'Paperlogy-5Medium',
          selectedDayBackgroundColor: '#7A5ADB',
          arrowColor: '#7A5ADB',
          dotColor: '#7A5ADB',
          todayTextColor: '#7A5ADB',
          textSectionTitleColor: 'black',
        }} 
        onDayPress={(day) => {
          handleDayPress(day)
        }} 
        dayComponent={({ date, state, marking }) => {
          const isSelected = date.dateString === selectedDate;
          return (
            <View style={{ alignItems: 'center', justifyContent: 'center',}}>
              <TouchableOpacity
              onPress={()=>handleDayPress(date)}>
                <View style={{ 
                  width: 20, 
                  height: 20, 
                  alignItems: 'center',
                  backgroundColor: isSelected ? '#7A5ADB' : 'transparent', 
                  borderRadius: 15, 
                  marginTop: 20,
                  }}>
                  <Text 
                  style={{
                    fontSize:14, 
                    fontFamily: 'Paperlogy-5Medium', 
                    color: isSelected ? 'white' : (state === 'disabled' ? 'lightgray' : 'black'),
                    }}>
                    {date.day}
                  </Text>
                  {marking?.emoji ? <Text style={{ fontSize: 18 }}>{marking.emoji}</Text> : null}
                </View>
              </TouchableOpacity>
              
            </View>

          )
          
        }}
          />
    </View>
  );
}
  
  const styles = StyleSheet.create({
    
  });


export default CalendarTab;