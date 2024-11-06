import React, { useState } from 'react';
import {StyleSheet, Text, View, TextInput, Image } from 'react-native';
import { createStackNavigator } from '@react-navigation/stack';
// import { styles } from '../../styles/style';
import CalendarTab from './calendarTab';
import WeatherTab from './weatherTab';
import DiaryView from './diaryView';

const MainStack = createStackNavigator();

function MainComponent({navigation}) {

  return (
        <View style={{flex: 1, backgroundColor: 'white'}}>
          <WeatherTab/>
          <CalendarTab navigation={navigation}/>
        </View>
  );
}

function HomeComponent() {

  return (
    <MainStack.Navigator screenOptions={{headerShown: false}}>
      <MainStack.Screen name='Home' component={MainComponent}/>
      <MainStack.Screen name='DiaryView' component={DiaryView}/>
    </MainStack.Navigator>
    
    
    
  );
}

const styles = StyleSheet.create({
  
});

export default HomeComponent;
