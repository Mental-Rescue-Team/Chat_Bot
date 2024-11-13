// src/api/auth.js
import axios from 'axios';
// import { useNavigate } from 'react-router-dom';

// 서버 URL을 설정합니다.
// const URL = 'http://ceprj.gachon.ac.kr:60016';

export const getTokens = async (username, password) => {
  try {
    const res = await axios.post(`/member/login`, {
      "username": username,
      "password": password,
    });

    console.log(res.data);
    // 서버에서 받은 토큰 데이터
    const { accessToken, refreshToken } = res.data.token;

    // 토큰을 localStorage에 저장
    localStorage.setItem(
      'Tokens',
      JSON.stringify({
        accessToken,
        refreshToken,
      })
    );

    const storedTokens = JSON.parse(localStorage.getItem('Tokens'));
    console.log("저장된 Tokens:", storedTokens);

    console.log("Access Token:", accessToken);
    console.log("Refresh Token:", refreshToken);
    alert("로그인 성공");

    // 로그인 성공 시 화면 이동
    return true;
  } catch (err) {
    console.error("로그인 오류:", err);
    alert("에러 발생");
    return false;
  }
};
