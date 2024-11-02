export const APIKEY = `AIzaSyAaC6VXIljH0kqxfteI8lGhejYNfj3H7mw`;
export const SIGNUP = `https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=${APIKEY}`;
export const SIGNIN = `https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=${APIKEY}`;

import AsyncStorage from "@react-native-async-storage/async-storage";

export const setTokens = async (values, callBack) => {
    const firstPair = ["@Project@userId", values.userId]
    const secondPair = ["@Project@token", values.token]
    const thirdPair = ["@Project@refToken", values.refToken]
    try {
      await AsyncStorage.multiSet([firstPair, secondPair, thirdPair]).then(response=>{callBack()})
    } catch(e) {
      //save error
    }
  
    console.log("Done.")
}

export const getTokens = async () => {

    let values
    try {
      values = await AsyncStorage.multiGet([
        '@Project@userId', 
        '@Project@token',
        '@Project@refToken'
    ])
    } catch(e) {
      // read error
    }
    console.log('Get Tokens: ', values);
  
    // example console.log output:
    // [ ['@MyApp_user', 'myUserValue'], ['@MyApp_key', 'myKeyValue'] ]
}

