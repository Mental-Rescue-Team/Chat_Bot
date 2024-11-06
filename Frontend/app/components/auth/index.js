import React, { useState, useEffect } from 'react';
import {StyleSheet, Text, View, ActivityIndicator, ScrollView} from 'react-native';
import AuthForm from './authForm';
import AsyncStorage from '@react-native-async-storage/async-storage';

const AuthComponent = ({navigation}) => {

    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                const tokenData = await AsyncStorage.getItem('Tokens');
                if (tokenData) {
                    const { accessToken } = JSON.parse(tokenData);
                    if (accessToken) {
                        // 액세스 토큰이 유효한 경우, AppTabComponent로 이동
                        navigation.navigate('AppTabComponent');
                    } else {
                        // 액세스 토큰이 없는 경우, 로그인 화면 유지
                        setLoading(false);
                    }
                } else {
                    // 토큰 데이터가 없는 경우, 로그인 화면 유지
                    setLoading(false);
                }
            } catch (err) {
                console.error("로그인 상태 확인 중 오류 발생:", err);
                setLoading(false);
            }
        };

        checkLoginStatus();
    }, [navigation]);
    
    if(loading) {
        return (
            <View style={styles.loading}>
                <ActivityIndicator/>
            </View>
        )
    }
    else {
        return (
            <View style={styles.container}>
                <AuthForm
                    navigation={navigation}
                />
            </View>
            
        )
    }
    
};

const styles = StyleSheet.create({
    loading: {
        flex: 1,
        backgroundColor: 'white',
        justifyContent: 'center',
        alignItems: 'center',
    },
    container: {
        flex: 1,
        backgroundColor: 'white',
    }
});

export default AuthComponent;
