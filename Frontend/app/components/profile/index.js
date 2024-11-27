import React, {useState, useEffect} from 'react';
import {Text, View, TouchableWithoutFeedback, Image, StyleSheet, Alert, ScrollView} from 'react-native';
import { styles } from '../../styles/style';
import { TextInput } from 'react-native-paper';
import Logo from '../../../android/app/src/main/assets/images/logo.png';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
const URL = 'http://ceprj.gachon.ac.kr:60016'

const ProfileComponent = ({navigation}) => {

    const [user, setUser] = useState("");
    const [email, setEmail] = useState("");
    const [birth, setBirth] = useState("");
    const [gender, setGender] = useState("");

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const tokenData = await AsyncStorage.getItem('Tokens');
                const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
                const accessToken = parsedTokenData?.accessToken;

                if (!accessToken) {
                console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
                alert("로그인이 필요합니다.");
                return;
                }

                console.log("Authorization 헤더:", `Bearer ${accessToken}`);

                if (accessToken) {
                    const response = await axios.get(`${URL}/member`, {
                        headers: {
                            Authorization: `Bearer ${accessToken}`, // Bearer 토큰 인증
                        },
                    });

                    console.log('서버 응답:', response.data);

                    // 프로필 정보를 상태에 저장
                    const { username, email, birth, gender } = response.data.data; // 서버에서 응답 받은 데이터
                    setUser(username);
                    setEmail(email);
                    setBirth(birth);
                    setGender(gender);
                } else {
                    Alert.alert("로그인 필요", "프로필 정보를 가져오기 전에 로그인해야 합니다.");
                    navigation.navigate('SignIn'); // 로그인 화면으로 이동
                }
            } catch (error) {
                console.error("프로필 정보를 가져오는 중 오류 발생:", error);
                Alert.alert("오류", "프로필 정보를 불러오는 데 실패했습니다.");
            }
        };

        fetchProfileData(); // 프로필 데이터 요청
    }, []);

    const handleLogout = async () => {
        try {
            await AsyncStorage.removeItem('Tokens'); // 저장된 토큰 삭제
            Alert.alert("로그아웃", "성공적으로 로그아웃되었습니다.");
            navigation.navigate('SignIn'); // 로그인 화면으로 이동
        } catch (error) {
            console.error("로그아웃 중 오류 발생:", error);
            Alert.alert("오류", "로그아웃에 실패했습니다.");
        }
    };


    return (
        <ScrollView style={{backgroundColor: 'white'}}>
            <View style = {styles.container}>
                <Image
                        style={styles.image}
                        source={Logo}
                        resizeMode='cover'
                    />
                <View style={styles.mainView}>
                    <Text style={styles.profileText}>Username</Text>
                    <TextInput style={styles.profileTextInput}
                        value={user}
                        onChangeText={user => setUser(user)}
                        mode='flat'
                        underlineColor="#7A5ADB"
                        editable={false}
                    />
                    <Text style={styles.profileText}>E-mail</Text>
                    <TextInput style={styles.profileTextInput}
                        value={email}
                        onChangeText={mail => setEmail(mail)}
                        mode='flat'
                        underlineColor="#7A5ADB"
                        editable={false}
                    />
                    <Text style={styles.profileText}>Birth</Text>
                    <TextInput style={styles.profileTextInput}
                        value={birth}
                        onChangeText={birth => setBirth(birth)}
                        mode='flat'
                        underlineColor="#7A5ADB"
                        editable={false}
                    />
                    <Text style={styles.profileText}>Gender</Text>
                    <TextInput style={styles.profileTextInput}
                        value={gender}
                        onChangeText={gender => setGender(gender)}
                        mode='flat'
                        underlineColor="#7A5ADB"
                        editable={false}
                    />
                    
                </View>
                <TouchableWithoutFeedback onPress={handleLogout}>
                    <View style={styles.logoutButton}>
                        <Text style={styles.logoutText}>로그아웃</Text>
                    </View>
                </TouchableWithoutFeedback>
                
            </View>
        </ScrollView>
    )
}

export default ProfileComponent;