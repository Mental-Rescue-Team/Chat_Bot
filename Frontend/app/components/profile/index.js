import React, {useState, useEffect} from 'react';
import {Text, View, TouchableWithoutFeedback, Image, StyleSheet, Alert} from 'react-native';
import { styles } from '../../styles/style';
import { TextInput } from 'react-native-paper';
import Logo from '../../assets/images/logo.png';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
const URL = 'https://a04db67f-68f0-4131-a8bd-0504a248a1ca.mock.pstmn.io'

const ProfileComponent = ({navigation}) => {

    const [user, setUser] = useState("");
    const [email, setEmail] = useState("");

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                // AsyncStorage에서 accessToken을 가져옵니다.
                const tokens = await AsyncStorage.getItem('Tokens');
                const parsedTokens = tokens ? JSON.parse(tokens) : null;
                // console.log(tokens);

                if (parsedTokens && parsedTokens.accessToken) {
                    const response = await axios.get(`${URL}/api/member`, {
                        headers: {
                            Authorization: `Bearer ${parsedTokens.accessToken}`, // Bearer 토큰 인증
                        },
                    });

                    // 프로필 정보를 상태에 저장
                    const { username, email } = response.data.data; // 서버에서 응답 받은 데이터
                    setUser(username);
                    setEmail(email);
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
        <View style = {styles.container}>
            <Image
                    style={styles.image}
                    source={Logo}
                    resizeMode='cover'
                />
            <View style={styles.mainView}>
                <Text style={styles.text}>Username</Text>
                <TextInput style={styles.textInput}
                    value={user}
                    onChangeText={user => setUser(user)}
                    mode='flat'
                    editable={false}
                />
                <Text style={styles.text}>E-mail</Text>
                <TextInput style={styles.textInput}
                    value={email}
                    onChangeText={mail => setMail(mail)}
                    mode='flat'
                    editable={false}
                />
                
            </View>
            {/* <TouchableWithoutFeedback>
                <View style={styles.button}>
                    <Text style={styles.buttext}>개인정보 수정</Text>
                </View>
            </TouchableWithoutFeedback> */}
            <TouchableWithoutFeedback onPress={handleLogout}>
                <View style={styles.logoutButton}>
                    <Text style={styles.logoutText}>로그아웃</Text>
                </View>
            </TouchableWithoutFeedback>
            
        </View>
    )
}






export default ProfileComponent;
