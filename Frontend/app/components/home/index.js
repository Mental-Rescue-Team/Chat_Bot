import React, { useCallback } from 'react';
import {StyleSheet, Text, View } from 'react-native';
import { createStackNavigator } from '@react-navigation/stack';
import { useFocusEffect } from '@react-navigation/native'; // useFocusEffect 추가
import CalendarTab from './calendarTab';
import WeatherTab from './weatherTab';
import DiaryView from './diaryView';
import { ScrollView } from 'react-native-gesture-handler';

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
      <MainStack.Screen name='Main' component={MainComponent}/>
      <MainStack.Screen name='DiaryView' component={DiaryView}/>
    </MainStack.Navigator>
    
    
    
  );
}

const styles = StyleSheet.create({
  
});

export default HomeComponent;
