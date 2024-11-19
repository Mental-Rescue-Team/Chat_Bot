import React, { useState, useEffect } from 'react';
import { Text, View, TouchableWithoutFeedback, Button, Image } from 'react-native';
import { styles } from '../../styles/style';
import { TextInput } from 'react-native-paper';
import { getTokens } from '../../utils/tokenUtils';
import Logo from '../../../android/app/src/main/assets/images/logo.png';


const AuthForm = ({navigation}) =>  {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const onPressLoginButton = () =>{
        getTokens(username, password, navigation);
    }

    const onPressJoinButton = () => {
        navigation.navigate('SignUp');
    }

    return (
        <View style = {styles.container}>
            <Image
                style={styles.image}
                source={Logo}
                resizeMode='cover'
            />
            <Text style={styles.mainText}>avery.</Text>
                <View style={styles.mainView}>
                    <TextInput style={styles.textInput}
                            value={username}
                            type="username"
                            onChangeText={username => setUsername(username)}
                            placeholder="Username"
                            mode='outlined'/>
                    <TextInput style={styles.textInput}
                            value={password}
                            type="password"
                            onChangeText={password => setPassword(password)}
                            placeholder="Password"
                            secureTextEntry
                            mode='outlined'/>
                </View>
            <TouchableWithoutFeedback onPress={onPressLoginButton}>
                <View style={styles.button}>
                    <Text style={styles.buttext}>로그인</Text>
                </View>
            </TouchableWithoutFeedback>
            <TouchableWithoutFeedback onPress={onPressJoinButton}>
                <View 
                // style={styles.button}
                >
                    <Text style={styles.text}>계정이 없으신가요? 가입하기</Text>
                </View>
            </TouchableWithoutFeedback>
        </View>
        
    )
}
    

export default AuthForm;
