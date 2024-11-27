import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, FlatList, StyleSheet, Alert, ImageBackground } from 'react-native';
import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from 'axios';
import Icon from 'react-native-vector-icons/Ionicons';

const ChatbotScreen = ({route, navigation}) => {
  const { mode } = route.params;
  const [messages, setMessages] = useState([]);
  const [inputText, setInputText] = useState('');
  const [backgroundEmotion, setBackgroundEmotion] = useState('');

  // 모드에 따라 초기 메시지 설정
  useEffect(() => {
    let initialMessage = '안녕하세요. 오늘은 무슨 일이신가요?'; // 기본 메시지

    switch (mode) {
      case 'counselor':
        initialMessage = '안녕하세요. 상담사가 도와드리겠습니다.';
        break;
      case 'friend':
        initialMessage = '안녕! 친구처럼 편하게 얘기해 봐! 뭐든 다 들어줄게!';
        break;
      case 'T':
        initialMessage = '안녕하세요. MBTI T 모드입니다. 오늘 하루를 얘기해보세요.';
        break;
      case 'F':
        initialMessage = '반가워요. MBTI F 모드에요. 오늘은 무슨 일인가요?';
        break;
      default:
        initialMessage = '안녕하세요. 어떤 도움을 드릴까요?';
    }

    setMessages([{ id: '1', text: initialMessage, isBot: true }]);
  }, [mode]);

  const getServerUrl = () => {
    switch (mode) {
      case 'counselor': return 'http://ceprj.gachon.ac.kr:60016/chatbot/counselor';
      case 'friend': return 'http://ceprj.gachon.ac.kr:60016/chatbot/friend';
      case 'T': return 'http://ceprj.gachon.ac.kr:60016/chatbot/T';
      case 'F': return 'http://ceprj.gachon.ac.kr:60016/chatbot/F';
      default: return 'http://ceprj.gachon.ac.kr:60016/chatbot/counselor';
    }
  };

  const getBackgroundData = async () => {
    try {
      const tokenData = await AsyncStorage.getItem('Tokens');
      const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
      const accessToken = parsedTokenData?.accessToken;

      if (!accessToken) {
        console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
        alert("로그인이 필요합니다.");
        return;
      }

      const serverUrl = 'http://ceprj.gachon.ac.kr:60016/diary/background';
      const response = await axios.get(serverUrl, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      console.log('Background Data:', response.data);
      setBackgroundEmotion(response.data);
    } catch (error) {
      console.error('Error fetching background data:', error);
    }
  };

  // 컴포넌트가 마운트되면 배경 데이터 가져오기
  useEffect(() => {
    getBackgroundData();
  }, []);

  const getBackgroundImage = (emotion) => {
    switch (emotion) {
      case '기쁨': return require('../../../android/app/src/main/assets/images/chat_sunny.png');
      case '슬픔': return require('../../../android/app/src/main/assets/images/chat_rain.png');
      case '평온': return require('../../../android/app/src/main/assets/images/chat_normal.png');
      case '분노': return require('../../../android/app/src/main/assets/images/chat_angry.png');
      case '불안': return require('../../../android/app/src/main/assets/images/chat_sad.png');
      default: return require('../../../android/app/src/main/assets/images/chat_default.png');
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
    <View style={{flex: 1}}>
    <ImageBackground
      source={getBackgroundImage(backgroundEmotion)}
      style={styles.backgroundImage}
      imageStyle={styles.imageStyle}
    >
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
      </View>
      </ImageBackground>
      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          value={inputText}
          onChangeText={setInputText}
          placeholder="Type a message..."
        />
        <TouchableOpacity onPress={sendMessage} style={styles.sendButton}>
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
    backgroundColor: 'rgba(255, 255, 255, 0.6)', // 흰색, 80% 불투명
    borderColor: '#ccc',
    borderWidth: 1,
  },
  userMessage: {
    alignSelf: 'flex-end',
    // backgroundColor: '#E0D1F5',
    backgroundColor: 'rgba(224, 209, 245, 0.6)', // 보라색, 80% 불투명
  },
  messageText: {
    color: 'black',
  },
  inputContainer: {
    flexDirection: 'row',
    padding: 10,
    borderTopWidth: 1,
    borderColor: '#ccc',
    backgroundColor: '#fff',
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
    padding: 8,
    borderRadius: 5,
    margin: 10,
    marginBottom: 5,
    alignItems: 'center',
  },
  reportButtonText: {
    color: '#fff',
    fontSize: 16,
    fontFamily: 'Paperlogy-6SemiBold',
  },
  backgroundImage: {
    flex: 1,
    resizeMode: 'cover', // 이미지 크기를 화면에 맞춤
    // justifyContent: 'flex-start',
  },
  imageStyle: {
    resizeMode: 'cover',
    alignSelf: 'flex-start', // 이미지가 상단 기준으로 맞춰지도록 설정
  },
  container: {
    flex: 1,
    backgroundColor: 'transparent', // 배경 투명 설정
  },
});

export default ChatbotScreen;
