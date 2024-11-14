import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, FlatList, StyleSheet, Alert } from 'react-native';
import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from 'axios';
import Icon from 'react-native-vector-icons/Ionicons';
import Logo from '../../assets/images/logo.png';

const ChatbotScreen = ({route, navigation}) => {
  const { mode } = route.params;
  const [messages, setMessages] = useState([
    { id: '1', text: '안녕하세요. 오늘은 무슨 일이신가요?', isBot: true },
  ]);
  const [inputText, setInputText] = useState('');

  const getServerUrl = () => {
    switch (mode) {
      case 'counselor': return 'http://ceprj.gachon.ac.kr:60016/chatbot/counselor';
      case 'friend': return 'http://ceprj.gachon.ac.kr:60016/chatbot/friend';
      case 'T': return 'http://ceprj.gachon.ac.kr:60016/chatbot/T';
      case 'F': return 'http://ceprj.gachon.ac.kr:60016/chatbot/F';
      default: return 'http://ceprj.gachon.ac.kr:60016/chatbot/counselor';
    }
  };


  // 메시지 전송 함수
  const sendMessage = async () => {
    if (inputText.trim() === '') return;

    // 사용자 메시지를 추가
    const userMessage = { id: Math.random().toString(), text: inputText, isBot: false };
    setMessages((prevMessages) => [...prevMessages, userMessage]);
    setInputText('');

    try {
      const tokenData = await AsyncStorage.getItem('Tokens');
      const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
      const accessToken = parsedTokenData?.accessToken;

      if (!accessToken) {
        console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
        alert("로그인이 필요합니다.");
        return;
      }

      console.log("보낼 데이터:", { inputText });
      console.log("Authorization 헤더:", `Bearer ${accessToken}`);
      // 서버로 메시지 전송 및 응답 수신
      const serverUrl = getServerUrl();
      const response = await axios.post(serverUrl, {
        "message": inputText
      },
      {
        headers: {
            Authorization: `Bearer ${accessToken}`,
        }
      });

      console.log('Server Response:', response.data);

      // 서버에서 응답을 받으면 봇 메시지 추가
      const botMessage = { 
        id: Math.random().toString(), 
        text: response.data.replace(/"/g, ''), 
        isBot: true 
      };
      setMessages((prevMessages) => [...prevMessages, botMessage]);
    } catch (error) {
      console.error('Error sending message:', error);
    }
  };

  // 메시지 아이템 렌더링
  const renderItem = ({ item }) => (
    <View style={[styles.messageContainer, item.isBot ? styles.botMessage : styles.userMessage]}>
      <Text style={styles.messageText}>{item.text}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <FlatList
        data={messages}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
      />
      {messages.filter(message => !message.isBot).length >= 2 && (
        <TouchableOpacity 
          style={styles.reportButton} 
          onPress={() => navigation.navigate('Report')}
        >
          <Text style={styles.reportButtonText}>레포트 화면으로 이동</Text>
        </TouchableOpacity>
      )}
      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          value={inputText}
          onChangeText={setInputText}
          placeholder="Type a message..."
        />
        <TouchableOpacity onPress={sendMessage} style={styles.sendButton}>
          {/* <Text style={styles.sendButtonText}>Send</Text> */}
          <Icon name='send' color='white' size={18}/>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  messageContainer: {
    marginVertical: 5,
    marginHorizontal: 10,
    padding: 10,
    borderRadius: 10,
    maxWidth: '80%',
  },
  botMessage: {
    alignSelf: 'flex-start',
    backgroundColor: 'white',
    borderColor: '#ccc',
    borderWidth: 1,
  },
  userMessage: {
    alignSelf: 'flex-end',
    backgroundColor: '#E0D1F5',
  },
  messageText: {
    color: 'black',
  },
  inputContainer: {
    flexDirection: 'row',
    padding: 10,
    borderTopWidth: 1,
    borderColor: '#ccc',
  },
  input: {
    flex: 1,
    height: 40,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 20,
    paddingHorizontal: 10,
    marginRight: 10,
  },
  sendButton: {
    backgroundColor: '#7A5ADB',
    borderRadius: 20,
    paddingVertical: 10,
    paddingHorizontal: 20,
  },
  sendButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  reportButton: {
    backgroundColor: '#7A5ADB',
    padding: 10,
    borderRadius: 5,
    margin: 10,
    alignItems: 'center',
  },
  reportButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default ChatbotScreen;
