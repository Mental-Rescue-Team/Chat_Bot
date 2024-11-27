import React from 'react';
import {View, Text, StyleSheet} from 'react-native';

function DateHead({date}) {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();

  const formatted = `${month}.${day}. ${year}`;

  return (
    <View style={styles.backGround}>
      <Text style={styles.date}>{formatted}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
    backGround: {
        // backgroundColor: '#F5F5F5',
        paddingVertical: 10,
        paddingHorizontal: 30,
        borderRadius: 10,
        marginBottom: 10,
        width: '70%',
        alignItems: 'flex-start',
    },
    date: {
        fontFamily: 'Paperlogy-8ExtraBold',
        fontSize: 30,
        color: '#7A5ADB',
    },
});

export default DateHead;