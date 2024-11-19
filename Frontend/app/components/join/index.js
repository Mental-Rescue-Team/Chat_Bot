import React, {useState, useEffect} from 'react';
import {StyleSheet, Text, View, TouchableWithoutFeedback} from 'react-native';
import { TextInput, RadioButton } from 'react-native-paper';
import { styles } from '../../styles/style';
import { setTokens } from '../../utils/tokenUtils';

const JoinTabComponent = ({navigation}) =>  {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [birth, setBirth] = useState("");
    const [gender, setGender] = useState('');

    const accessJoin = () => {
        setTokens(username, email, birth, password, gender, navigation);
    }


  return (
    <View style = {styles.container}>
        <Text style={styles.mainText}>Sign Up</Text>
        <View style={styles.mainView}>
            <TextInput style={styles.textInput}
                    value={username}
                    onChangeText={username => setUsername(username)}
                    placeholder="Username"
                    mode='outlined'/>
            <TextInput style={styles.textInput}
                    value={password}
                    type="password"
                    onChangeText={password => setPassword(password)}
                    placeholder="Password"
                    mode='outlined'
                    secureTextEntry/>
            <TextInput style={styles.textInput}
                    value={birth}
                    onChangeText={birth => setBirth(birth)}
                    placeholder="Birth"
                    mode='outlined'/>
            <TextInput style={styles.textInput}
                    value={email}
                    type="email"
                    onChangeText={email => setEmail(email)}
                    placeholder="E-mail"
                    mode='outlined'/>
            <Text style={styles.text}>성별</Text>
                {/* <RadioButton.Group>
                    <View style={{ flexDirection: 'row', alignItems: 'center'}}>
                        <RadioButton
                            value="first"
                            status={ gender === '남성' ? 'checked' : 'unchecked' }
                            onPress={() => setGender('남성')}
                        />
                        <Text style={styles.text}>남성</Text>
                        <RadioButton
                            value="second"
                            status={ gender === '여성' ? 'checked' : 'unchecked' }
                            onPress={() => setGender('여성')}
                        />
                        <Text style={styles.text}>여성</Text>
                    </View>
                </RadioButton.Group> */}
            <RadioButton.Group 
                value={gender} 
                onValueChange={newValue => setGender(newValue)} // gender 값을 업데이트
            >
                <View style={{ flexDirection: 'row', alignItems: 'center'}}>
                    <RadioButton
                        value="male" // 남성을 선택하면 "남성"이 gender로 설정됨
                    />
                    <Text style={styles.text}>남성</Text>
                    
                    <RadioButton
                        value="female" // 여성을 선택하면 "여성"이 gender로 설정됨
                    />
                    <Text style={styles.text}>여성</Text>
                </View>
            </RadioButton.Group>
        </View>
        <TouchableWithoutFeedback onPress={accessJoin}>
            <View style={styles.button}>
                <Text style={styles.buttext}>가입 완료</Text>
            </View>
        </TouchableWithoutFeedback>
        </View>
  );
};

export default JoinTabComponent;