import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
// const URL = 'https://a04db67f-68f0-4131-a8bd-0504a248a1ca.mock.pstmn.io'
const URL = 'http://ceprj.gachon.ac.kr:60016'

export const setTokens = (username, email, birth, password, gender, navigation) => {
    axios.post(`${URL}/member/register`,   
    {
        "username":username,
        "password":password,
        "email":email,
        "birth":birth,
        "gender":gender
    })
    .then(res=>{
        console.log(res.data);
        alert("회원가입 성공");
        
        navigation.navigate('SignIn');
    
    }).catch(err=>{
        alert("에러 발생");
        console.error("오류:", err);
        return false;
    })
};

export const getTokens = async (username, password, navigation) => {
    try {
      const res = await axios.post(`${URL}/member/login`, 
        {
          "username":username,
          "password":password
        });

      console.log(res.data);
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

      const storedTokens = await AsyncStorage.getItem('Tokens');
      console.log("저장된 Tokens:", JSON.parse(storedTokens));
  
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

export const diaryData = async (message, navigation) => {
    try {
        const tokenData = await AsyncStorage.getItem('Tokens');
        const parsedTokenData = tokenData ? JSON.parse(tokenData) : null;
        const accessToken = parsedTokenData?.accessToken;

        if (!accessToken) {
          console.error('Access token이 없습니다. 로그인 후 다시 시도하세요.');
          alert("로그인이 필요합니다.");
          return;
        }

        console.log("보낼 데이터:", { message });
        console.log("Authorization 헤더:", `Bearer ${accessToken}`);

        const response = await axios.post(`${URL}/diary`, 
            {
              message
            },
            {
              headers: {
                  Authorization: `Bearer ${accessToken}`,
              }
            });

        console.log('서버 응답:', response.data);
        alert("저장되었습니다");
        // navigation.navigate('DiaryView');

    } catch (error) {
      console.error('데이터 전송 실패:', error);
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