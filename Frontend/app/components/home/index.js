import React, { useState } from 'react';
import {StyleSheet, Text, View, TextInput, Image } from 'react-native';
// import { styles } from '../../styles/style';
import CalendarTab from './calendarTab';
import WeatherTab from './weatherTab';

function HomeComponent() {

  return (
    <View style={{flex: 1, backgroundColor: 'white'}}>
      <WeatherTab/>
      <CalendarTab/>
    </View>
    
    
  );
}

const styles = StyleSheet.create({
  
});

export default HomeComponent;
