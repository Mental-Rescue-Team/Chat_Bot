import React, { useState } from 'react';
import {StyleSheet, Text, View, TextInput, ScrollView, Image} from 'react-native';
import DateHead from '../diary/dateHead';
import Logo from '../../assets/images/logo.png';

const DiaryView = () =>  {

  const [text, setText] = useState('');
  const [image, setImage] = useState(Logo);

  const today = new Date();

  const onChangeText = (inputText) => {
    setText(inputText);
  };

  return (
    <View style={{flex: 1, backgroundColor: 'white', justifyContent: 'center'}}>
      <ScrollView>
        <View style={{justifyContent: 'flex-start', padding: 20}}>
          <DateHead date={today}/>
        </View>
        <View style={{justifyContent: 'center', alignItems: 'center',}}>
          <TextInput
                  onChangeText={onChangeText}
                  value={text}
                  placeholder="오늘 작성한 일기입니다"
                  placeholderTextColor={'grey'}
                  style={styles.input}
                  multiline
                />
          {/* <Image
            source={image}
            resizeMode={'contain'}
            style={{
                width: '90%'
            }}
        />           */}
        </View>
      </ScrollView>  
    </View>
  );
};

const styles = StyleSheet.create({
  input: {
    width: '90%',
    height: 300,
    fontSize: 16,
    borderColor: '#999',
    borderWidth: 1,
    borderRadius: 10,
    padding: 10,
    textAlignVertical: 'top',
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#7A5ADB',
    paddingVertical: 15,
    paddingHorizontal: 30,
    borderRadius: 10,
    marginBottom: 20,
    width: '90%',
    alignItems: 'center',
  },
  buttext: {
    color: 'white',
    fontSize: 15,
    fontWeight: 'bold',
  },
  mainText: {
    fontFamily: 'Paperlogy-7Bold',
    fontSize: 30,
    padding: 20,
    color: '#7A5ADB',
  },
});

export default DiaryView;