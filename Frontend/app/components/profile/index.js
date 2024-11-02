/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */


import * as React from 'react';
import {Text, View, TouchableWithoutFeedback, Image, StyleSheet} from 'react-native';
import { styles } from '../../styles/style';
import { TextInput } from 'react-native-paper';
import Logo from '../../assets/images/logo.png';

const ProfileComponent = ({navigation}) => {

    const [user, setUser] = React.useState("rsy991225_user");

    const [mail, setMail] = React.useState("rsy991225@gachon.ac.kr");


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
                />
                <Text style={styles.text}>E-mail</Text>
                <TextInput style={styles.textInput}
                    value={mail}
                    onChangeText={mail => setMail(mail)}
                    mode='flat'
                />
                
            </View>
            {/* <TouchableWithoutFeedback>
                <View style={styles.button}>
                    <Text style={styles.buttext}>개인정보 수정</Text>
                </View>
            </TouchableWithoutFeedback> */}
            <TouchableWithoutFeedback onPress={()=>navigation.navigate('SignIn')}>
                <View style={styles.logoutButton}>
                    <Text style={styles.logoutText}>로그아웃</Text>
                </View>
            </TouchableWithoutFeedback>
            
        </View>
    )
}






export default ProfileComponent;
