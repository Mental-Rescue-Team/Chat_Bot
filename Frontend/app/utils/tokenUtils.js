import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
import { Toast } from "react-native-toast-message/lib/src/Toast";
const URL = 'https://a04db67f-68f0-4131-a8bd-0504a248a1ca.mock.pstmn.io'
// const URL = 'http://ceprj.gachon.ac.kr:60016'

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

// export const getTokens = (username, password, navigation) => {
//     axios.post(`${URL}/api/auth/login`, 
//     {
//       "username":username,
//       "password":password
//     })
//     .then(res=>{{
//         // if (res.status === 200){
//         //     AsyncStorage.setItem('Tokens', JSON.stringify({
//         //     //   'accessToken': res.data.accessToken,
//         //     //   'refreshToken': res.data.refreshToken,
//         //     //   'userId': res.data.userId
//         //     "token": res.data
//         //     }))
//         // }
//         console.log(res.data);
//         alert("로그인 성공");
        
//         navigation.navigate('AppTabComponent');
//     }
//         // return response.data;
//     }).catch(err=>{
//         alert("에러 발생");
//         return false;
//     })
// };

export const getTokens = async (username, password, navigation) => {
    try {
      const res = await axios.post(`${URL}/api/auth/login`, {
        "username":username,
        "password":password
      });
      // 서버에서 받은 토큰 데이터
      const { accessToken, refreshToken } = res.data.token;
  
      // 토큰을 AsyncStorage에 저장
      await AsyncStorage.setItem(
        'Tokens',
        JSON.stringify({
          accessToken,
          refreshToken
        })
      );
  
      console.log("Access Token:", accessToken);
      console.log("Refresh Token:", refreshToken);
      alert("로그인 성공");
  
      // 로그인 성공 시 화면 이동
      navigation.navigate('AppTabComponent');
    } catch (err) {
      console.error("로그인 오류:", err);
      alert("에러 발생");
      return false;
    }
};

// export const diaryData = (text, navigation) => {
//     axios.post(`${URL}/api/diary`,
//         {
//             "text":text
//         }
//     )
//     .then(res=>{{
//         if (res.status === 200){
//             AsyncStorage.setItem('DiaryData', JSON.stringify({
//               'date': res.data.date,
//               'emotion': res.data.emotion,
//               'weather': res.data.weather,
//               'emoji': res.data.emoji
//             }))
//         }

//         console.log(res.data);
//         alert("저장되었습니다");
        
//         navigation.navigate('Home');
        
//     }
//         // return response.data;
//     }).catch(err=>{
//         alert("에러 발생");
//         return false;
//     })
// }

export const diaryData = async (formattedDate, text, navigation) => {
    try {
      const res = await axios.post(`${URL}/api/diary`, {
        "date": formattedDate,
        "text": text
      });
  
      if (res.status === 200) {
        try {
          await AsyncStorage.setItem('DiaryData', JSON.stringify({
            'date': res.data.date,
            'emotion': res.data.emotion,
            // 'weather': res.data.weather,
            // 'emoji': res.data.emoji
          }));
          console.log(res.data);
          alert("저장되었습니다");
          navigation.navigate('Home');
        } catch (storageError) {
          console.error('Error saving data to AsyncStorage:', storageError);
          alert("저장 중 에러가 발생했습니다.");
        }
      }
    } catch (err) {
      console.error('Error during request:', err);
      alert("에러 발생");
      return false;
    }
  };

  export const loadDiaryData = async () => {
    try {
      // 저장된 모든 키를 가져와서 출력
      const keys = await AsyncStorage.getAllKeys();
      console.log('Stored keys in AsyncStorage:', keys);
  
      const value = await AsyncStorage.getItem("DiaryData");
      if (value !== null) {
        console.log('Raw value from AsyncStorage:', value); // value 출력
  
        try {
          const parsedValue = JSON.parse(value);
          console.log('Parsed value from AsyncStorage:', parsedValue); // 파싱된 value 출력
          return parsedValue;
        } catch (parseError) {
          console.error('Error parsing stored data:', parseError);
          return null; // 파싱 오류 시 null 반환
        }
      } else {
        console.log('No data found for key: DiaryData');
        return null; // 데이터가 없을 경우
      }
    } catch (e) {
      console.error('Error loading data from AsyncStorage:', e);
      return null; // 에러 발생 시 null 반환
    }
  };

// export const loadDiaryData = async () => {
//   try {
//     const value = await AsyncStorage.getItem("DiaryData");
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