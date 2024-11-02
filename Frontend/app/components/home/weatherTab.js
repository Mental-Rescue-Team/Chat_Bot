import React, {useState} from 'react';
import {StyleSheet, Text, TextInput, View, Image} from 'react-native';
import WeatherImage from '../../assets/images/sunny.jpg'

const WeatherTab = () =>  {

  const [message, setMessage] = useState('오늘의 기분을 알려주세요.');

  return (
    <View style={{flex: 1, flexDirection: 'row', backgroundColor: 'white'}}>
      <View style={styles.container}>
        <Text style={styles.mainText}>Today's My Weather</Text>
        <Text style={styles.subText}>{message}</Text>
        {/* <TextInput
        style={styles.input}
        placeholder="오늘 당신의 기분을 알려주세요."
        placeholderTextColor={'black'}
        multiline={true}
        /> */}
      </View>
      <View style={styles.image}>
        <Image
            source={WeatherImage}
            resizeMode={'contain'}
            style={{
                width: 150
            }}
        />      
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 2.5,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20
  },
  input: {
    width: 250,
    height: 100,
    borderColor: '#999',
    borderWidth: 1,
    borderRadius: 10,
    padding: 10,
    textAlignVertical: 'top',
    marginTop: 20,
    fontFamily: 'Paperlogy-7Bold',
  },
  image: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20
  },
  mainText: {
    fontFamily: 'Paperlogy-7Bold',
    fontSize: 20,
    color: '#7A5ADB',
  },
  subText: {
    fontFamily: 'Paperlogy-7Bold',
    fontSize: 15,
    color: 'black',
    padding: 10,
  },
});

export default WeatherTab;