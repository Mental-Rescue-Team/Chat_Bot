/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */


import React, { useState, useCallback, useEffect } from 'react';
import {StyleSheet, Text, View} from 'react-native';
import { GiftedChat } from 'react-native-gifted-chat';

const ChatComponent = () =>  {

  const [messages, setMessages] = useState([]);

  useEffect(() => {
    setMessages([
      {
        _id: 1,
        text: 'Hello! Welcome to the chat!',
        createdAt: new Date(),
        user: {
          _id: 2,
          name: 'Chat Bot',
          avatar: 'https://placeimg.com/140/140/any',
        },
      },
    ]);
  }, []);

  const onSend = useCallback((newMessages = []) => {
    setMessages((previousMessages) => GiftedChat.append(previousMessages, newMessages));
  }, []);

  return (
    <GiftedChat 
        
        messages={messages} 
        onSend={(messages) => onSend(messages)}
        user={{_id: 1}}
        // renderAvatar={(props) => <CustomAvatar {...props} />}
        // renderBubble={(props) => <CustomBubble {...props} />}
        // renderInputToolbar={(props) => <CustomInputToolbar {...props} />}
        // renderSend={(props) => <CustomSendButton {...props} />}
        // loadEarlier={true}
        // onLoadEarlier={() => console.log('Load earlier')}
        // scrollToBottom={true}
        // isTyping={true}
        // dateFormat="LL"
        // timeFormat="LT"
        // locale="en"
      />
  );
};

const styles = StyleSheet.create({

});

export default ChatComponent;
