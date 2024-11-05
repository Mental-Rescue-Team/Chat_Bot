import React, { useState, useEffect } from 'react';
import { format } from "date-fns";
import {StyleSheet, Text, View, TouchableOpacity} from 'react-native';
import { Calendar, LocaleConfig } from 'react-native-calendars';
import { loadDiaryData } from '../../utils/tokenUtils';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
const URL = 'https://a04db67f-68f0-4131-a8bd-0504a248a1ca.mock.pstmn.io'

function CalendarTab({navigation}) {

  const [selectedDate, setSelectedDate] = useState(
    format(new Date(), "yyyy-MM-dd"),
  );
  const [emotionData, setEmotionData] = useState({});
  const [lastClickedDate, setLastClickedDate] = useState(null);

  const emotionToEmoji = {
    "기쁨": '😊',
    "슬픔": '😢',
    "평온": '😌',
    "분노": '😠',
    "불안": '😨',
  };

  // useEffect(() => {
  //   const fetchData = async () => {
  //     try {
  //       const data = await loadDiaryData();
  //       if (data && typeof data === 'object') {
  //         const { date, emotion } = data;
  //         const emoji = emotionToEmoji[emotion];
  //         setEmotionData({ [date]: { emoji } });
  //         console.log('Loaded emotion data for calendar:', { [date]: { emoji } });
  //       } else {
  //         console.log('No emoji data found or data format is incorrect:', data);
  //       }
  //     } catch (error) {
  //       console.error('Error loading diary data:', error);
  //     }
  //   };

  //   fetchData();
  // }, []);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // **서버에서 데이터 요청**
        const response = await axios.get(`${URL}/api/every/weathers`);
        const data = response.data;  // 서버로부터 받은 데이터

        // **AsyncStorage에 저장**
        await AsyncStorage.setItem('emotionData', JSON.stringify(data));

        // **emotionData 상태 설정** (각 날짜에 맞는 이모지 저장)
        const emotionMap = data.reduce((acc, entry) => {
          acc[entry.date] = { emoji: emotionToEmoji[entry.emotion] };
          return acc;
        }, {});
        setEmotionData(emotionMap);

      } catch (error) {
        console.error('데이터를 불러오는 중 오류 발생:', error);
      }
    };

    fetchData();
  }, []);

  const handleDayPress = (day) => {
    if (lastClickedDate === day.dateString) {
      navigation.navigate('DiaryView'); 
    } else {
      setLastClickedDate(day.dateString); 
      setSelectedDate(day.dateString);
    }
    // setSelectedDate(day.dateString);
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
                  {marking?.emoji ? <Text style={{ fontSize: 16 }}>{marking.emoji}</Text> : null}
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