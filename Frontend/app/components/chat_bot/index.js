import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';

const ModeSelectionScreen = ({ navigation }) => {
  const handleModeSelect = (mode) => {
    navigation.navigate('ChatBot', { mode });
  };

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
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
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
    fontFamily: 'Paperlogy-5Medium',
  },
});

export default ModeSelectionScreen;
