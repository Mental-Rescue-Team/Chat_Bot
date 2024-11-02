import React, { useState } from 'react';
import { format } from "date-fns";
import {StyleSheet, Text, View} from 'react-native';
import { Calendar, LocaleConfig } from 'react-native-calendars';

function CalendarTab() {

  const [emoji, setEmoji] = useState('');

  const posts = [
    {
      id: 1,
      title: "제목입니다.",
      contents: "내용입니다.",
      date: "2024-10-26",
    },
    {
      id: 2,
      title: "제목입니다.",
      contents: "내용입니다.",
      date: "2024-10-27",
    }
  ];
  const markedDates = posts.reduce((acc, current) => {
    const formattedDate = format(new Date(current.date), 'yyyy-MM-dd');
    acc[formattedDate] = {marked: true};
    return acc;
  }, {});

  const [selectedDate, setSelectedDate] = useState(
    format(new Date(), "yyyy-MM-dd"),
  );
  const markedSelectedDates = {
    ...markedDates,
    [selectedDate]: {
      selected: true,
      marked: markedDates[selectedDate]?.marked,
    }
  }

  return (
    <View style={{flex: 2}}>
        <Calendar 
        markedDates={markedSelectedDates}
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
          setSelectedDate(day.dateString)
        }} 
        // dayComponent={()=>{
        //   return (
        //     <View>
        //       <Text>🌞</Text>
        //     </View>
        //   )}}
          />
    </View>
  );
}
  
  const styles = StyleSheet.create({
    
  });


export default CalendarTab;