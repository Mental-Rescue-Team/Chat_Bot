import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
import { Toast } from "react-native-toast-message/lib/src/Toast";
const URL = 'https://a04db67f-68f0-4131-a8bd-0504a248a1ca.mock.pstmn.io'

// const showToast = (text) =>{
//     Toast.show({
//         type: 'error',
//         position: 'bottom',
//         text1: text,
//       });
// };

export const setTokens = (username, email, birth, password, navigation) => {
    axios.post(`${URL}/api/member/register`,
    {
        "username":username,
        "email":email,
        "birth":birth,
        "password":password
    })
    .then(res=>{{
        // if (res.status === 200){
        //     AsyncStorage.setItem('Tokens', JSON.stringify({
        //     //   'accessToken': res.data.accessToken,
        //     //   'refreshToken': res.data.refreshToken,
        //     //   'userId': res.data.userId
        //     "token": res.data
        //     }))
        // }
        console.log(res.data);
        alert("회원가입 성공");
        
        navigation.navigate('AppTabComponent');
    }
        // return response.data;
    }).catch(err=>{
        alert("에러 발생");
        return false;
    })
};

export const getTokens = (username, password, navigation) => {
    axios.post(`${URL}/api/auth/login`, 
    {
      "username":username,
      "password":password
    })
    .then(res=>{{
        // if (res.status === 200){
        //     AsyncStorage.setItem('Tokens', JSON.stringify({
        //     //   'accessToken': res.data.accessToken,
        //     //   'refreshToken': res.data.refreshToken,
        //     //   'userId': res.data.userId
        //     "token": res.data
        //     }))
        // }
        console.log(res.data);
        alert("로그인 성공");
        
        navigation.navigate('AppTabComponent');
    }
        // return response.data;
    }).catch(err=>{
        alert("에러 발생");
        return false;
    })
};

// const getTokenFromLocal = async () => {
//   try {
//     const value = await AsyncStorage.getItem("Tokens");
//     if (value !== null) {
//       return JSON.parse(value)
//     }
//     else{
//       return null;
//     }
//   } catch (e) {
//     console.log(e.message);
//   }
// };


// export const verifyTokens = async (navigation) => {
//   const Token = await getTokenFromLocal();

//   // 최초 접속
//   if (Token === null){
//     navigation.reset({routes: [{name: "AuthPage"}]});
//   }
//   // 로컬 스토리지에 Token데이터가 있으면 -> 토큰들을 헤더에 넣어 검증 
//   else{
//     const headers_config = {
//       "refresh": Token.refreshToken,
//       Authorization: `Bearer ${Token.accessToken}`   
//     };

//     try {
//       const res = await axios.get(`${URL}/refresh`, {headers: headers_config})

//       // accessToken 만료, refreshToken 정상 -> 재발급된 accessToken 저장 후 자동 로그인
//       AsyncStorage.setItem('Tokens', JSON.stringify({
//         ...Token,
//         'accessToken': res.data.data.accessToken,
//       }))
//       navigation.reset({routes: [{name: "HomeTab"}]});

//     } catch(error){
//       const code = error.response.data.code; 

//       // accessToken 만료, refreshToken 만료 -> 로그인 페이지
//       if(code === 401){
//         navigation.reset({routes: [{name: "AuthPage"}]});
//       }
//       // accessToken 정상, refreshToken 정상 -> 자동 로그인
//       else{
//         navigation.reset({routes: [{name: "HomeTab"}]});
//       }
//     }

//   }
// };