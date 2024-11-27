import * as React from 'react';
import { createStackNavigator } from '@react-navigation/stack'; 
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import Icon from 'react-native-vector-icons/Ionicons';

//Screens
import SignIn from './components/auth';
import SignUp from './components/join';
import Profile from './components/profile';
import Home from './components/home';
import Diary from './components/diary';
import ChatBot from './components/chat_bot/chatbot';
import Check from './components/check';
import ModeSelect from './components/chat_bot';
import Report from './components/chat_bot/report';

const AuthStack = createStackNavigator();
const MainScreenTab = createBottomTabNavigator();
const ChatStack = createStackNavigator();  

const isLoggedIn = false;

const ChatStackComponent = () => {
    return (
        <ChatStack.Navigator screenOptions={{ headerShown: false }}>
            <ChatStack.Screen name="ModeSelect" component={ModeSelect} />
            <ChatStack.Screen name="ChatBot" component={ChatBot} />
            <ChatStack.Screen name="Report" component={Report} />
        </ChatStack.Navigator>
    );
};

const AppTabComponent = () => {
    return (
        <MainScreenTab.Navigator screenOptions={({ route, navigation }) => ({
            headerShown: false,
            tabBarStyle: {
                height: 50,       
            },
            tabBarItemStyle: {
                borderTopWidth: 1,
                borderTopColor: navigation.getState().routes[navigation.getState().index].name === route.name 
                    ? getBorderColor(route.name) 
                    : 'transparent',
                marginHorizontal: 15,
            },
            tabBarActiveBackgroundColor: 'white',
            tabBarActiveTintColor: '#7A5ADB',
            tabBarInactiveTintColor: 'black',
            tabBarLabelStyle: {
                fontSize: 11,             // 라벨 텍스트 크기
                // fontWeight: '600',       // 라벨 텍스트 굵기
                marginBottom: 4
            },
            tabBarIconStyle: {
                marginTop: 4,            // 아이콘 위쪽 여백
            },
            })}>
            <MainScreenTab.Screen name="Home" component={Home}
            options={{
                title: '홈', 
                unmountOnBlur: true,
                tabBarIcon: ({color, size}) => (
                  <Icon name="home" color={color} size={size} />
                )}}/>
            <MainScreenTab.Screen name="Diary" component={Diary}
            options={{
                title: '일기', 
                tabBarIcon: ({color, size}) => (
                  <Icon name="book" color={color} size={size} />
                )}}/>
            <MainScreenTab.Screen name="Chat" component={ChatStackComponent}
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

const getBorderColor = (routeName) => {
    switch (routeName) {
        case 'Home':
            return '#7A5ADB'; // 홈 탭의 경계선 색
        case 'Diary':
            return '#7A5ADB'; // 일기 탭의 경계선 색
        case 'Chat':
            return '#7A5ADB'; // Chat 탭의 경계선 색
        case 'Check':
            return '#7A5ADB'; // 진단검사 탭의 경계선 색
        case 'Profile':
            return '#7A5ADB'; // 마이페이지 탭의 경계선 색
        default:
            return 'transparent';
    }
};

export const RootNavigator = () => {
    return (
        <AuthStack.Navigator
        screenOptions={{headerShown: false}}
        >
            {isLoggedIn ? (
                <>
                    <AuthStack.Screen name="AppTabComponent" component={AppTabComponent}/>
                </>
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