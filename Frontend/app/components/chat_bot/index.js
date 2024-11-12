import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';

const ModeSelectionScreen = ({ navigation }) => {
  const handleModeSelect = (mode) => {
    navigation.navigate('ChatBot', { mode });
  };

  // return (
  //   <View style={styles.container}>
  //     <Text style={styles.title}>모드를 선택하세요</Text>
  //     <TouchableOpacity onPress={() => handleModeSelect('counselor')} style={styles.button}>
  //       <Text style={styles.buttonText}>상담사 모드</Text>
  //     </TouchableOpacity>
  //     <TouchableOpacity onPress={() => handleModeSelect('friend')} style={styles.button}>
  //       <Text style={styles.buttonText}>친구 모드</Text>
  //     </TouchableOpacity>
  //     <TouchableOpacity onPress={() => handleModeSelect('T')} style={styles.button}>
  //       <Text style={styles.buttonText}>T 모드</Text>
  //     </TouchableOpacity>
  //     <TouchableOpacity onPress={() => handleModeSelect('F')} style={styles.button}>
  //       <Text style={styles.buttonText}>F 모드</Text>
  //     </TouchableOpacity>
  //   </View>
  // );

  return (
    <View style={styles.container}>
      <View style={styles.card}>
        <Text style={styles.title}>Chatting Mode</Text>
        <Text style={styles.subtitle}>Choose Your Mode</Text>
        
        <TouchableOpacity onPress={() => handleModeSelect('counselor')} style={styles.button}>
          <Text style={styles.buttonText}>일반 상담사 모드</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => handleModeSelect('friend')} style={styles.button}>
          <Text style={styles.buttonText}>친근한 친구 모드</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => handleModeSelect('T')} style={styles.button}>
          <Text style={styles.buttonText}>MBTI T 모드</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => handleModeSelect('F')} style={styles.button}>
          <Text style={styles.buttonText}>MBTI F 모드</Text>
        </TouchableOpacity>
        
        {/* <View style={styles.footer}>
          <TouchableOpacity onPress={() => navigation.na()}>
            <Text style={styles.footerText}>뒤로가기</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={() => handleModeSelect()}>
            <Text style={styles.footerText}>선택하기</Text>
          </TouchableOpacity>
        </View> */}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  // container: { 
  //   flex: 1, 
  //   justifyContent: 'center', 
  //   alignItems: 'center' 
  // },
  // title: { 
  //   fontSize: 24, 
  //   marginBottom: 20 
  // },
  // button: { 
  //   marginVertical: 10, 
  //   padding: 15, 
  //   backgroundColor: '#7A5ADB', 
  //   borderRadius: 5 
  // },
  // buttonText: { 
  //   color: '#fff', 
  //   fontSize: 16 
  // },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  card: {
    width: '80%',
    padding: 20,
    backgroundColor: '#EDE8FC',
    borderRadius: 20,
    alignItems: 'center',
  },
  title: {
    fontSize: 24,
    fontFamily: 'Paperlogy-7Bold',
    color: '#7A5ADB',
    marginBottom: 5,
  },
  subtitle: {
    fontSize: 16,
    color: '#9A8BC2',
    marginBottom: 20,
  },
  button: {
    width: '100%',
    padding: 15,
    marginVertical: 5,
    backgroundColor: 'white',
    borderRadius: 10,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowOffset: { width: 0, height: 3 },
    shadowRadius: 5,
  },
  buttonText: {
    color: '#5A4DAA',
    fontSize: 16,
  },
  // footer: {
  //   flexDirection: 'row',
  //   justifyContent: 'space-between',
  //   width: '100%',
  //   marginTop: 20,
  // },
  // footerText: {
  //   color: '#5A4DAA',
  //   fontSize: 16,
  // },
});

export default ModeSelectionScreen;
