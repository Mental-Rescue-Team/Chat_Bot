import * as React from 'react';

import { createStackNavigator } from '@react-navigation/stack'; 
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';

//Screens
import SignIn from './components/auth';
import SignUp from './components/join';
import Profile from './components/profile';
import Home from './components/home';
import Diary from './components/diary';
import ChatBot from './components/chat_bot';
import Check from './components/check';
import Icon from 'react-native-vector-icons/Ionicons';

const AuthStack = createStackNavigator();
const MainScreenTab = createBottomTabNavigator();  

const isLoggedIn = false;

const AppTabComponent = () => {
    return (
        <MainScreenTab.Navigator screenOptions={{
            headerShown: false,
            tabBarActiveBackgroundColor: '#7A5ADB',
            tabBarActiveTintColor: 'white',
            tabBarInactiveTintColor: 'black',
            }}>
            <MainScreenTab.Screen name="Home" component={Home}
            options={{
                title: '홈', 
                tabBarIcon: ({color, size}) => (
                  <Icon name="home" color={color} size={size} />
                )}}/>
            <MainScreenTab.Screen name="Diary" component={Diary}
            options={{
                title: '일기', 
                tabBarIcon: ({color, size}) => (
                  <Icon name="book" color={color} size={size} />
                )}}/>
            <MainScreenTab.Screen name="Chat" component={ChatBot}
            options={{
                title: 'Chat', 
                tabBarIcon: ({color, size}) => (
                  <Icon name="chatbubbles-sharp" color={color} size={size} />
                )}}/>
            <MainScreenTab.Screen name="Check" component={Check}
            options={{
                title: '진단검사', 
                tabBarIcon: ({color, size}) => (
                  <Icon name="document-text" color={color} size={size} />
                )}}/>
            <MainScreenTab.Screen name="Profile" component={Profile}
            options={{
                title: '마이페이지', 
                tabBarIcon: ({color, size}) => (
                  <Icon name="person" color={color} size={size} />
                )}}/>
        </MainScreenTab.Navigator>
    )
}

export const RootNavigator = () => {
    return (
        <AuthStack.Navigator
        screenOptions={{headerShown: false}}
        >
            {isLoggedIn ? (
                <AuthStack.Screen name="Main" component={AppTabComponent}/>
            ) : ( 
                <>       
                    <AuthStack.Screen name="SignIn" component={SignIn}/>
                    <AuthStack.Screen name="SignUp" component={SignUp}/>
                    <AuthStack.Screen name="AppTabComponent" component={AppTabComponent}/>
                </>
                
            )}
        </AuthStack.Navigator>
    )
}