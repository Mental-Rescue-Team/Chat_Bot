import React, { useEffect, useState } from 'react';
import axios from 'axios';

function StatisticsPage() {
  const [statistics, setStatistics] = useState(null);  // 서버에서 받은 통계 데이터를 저장
  const [error, setError] = useState(null);  // 에러 상태

  useEffect(() => {
    // localStorage에서 accessToken 가져오기
    const storedTokens = JSON.parse(localStorage.getItem('Tokens'));
    if (storedTokens && storedTokens.accessToken) {
      const token = storedTokens.accessToken;

      // 서버의 통계 엔드포인트 URL을 입력하세요
      axios.get('/admin/emotions', {
        headers: {
          Authorization: `Bearer ${token}` // accessToken을 Authorization 헤더에 추가
        }
      })
        .then(response => {
          // 서버에서 받은 데이터 콘솔에 출력
          console.log('Received statistics data:', response.data);

          // 서버에서 받은 데이터를 그대로 사용
          // '슬픔.' 처리는 불필요하므로 필터링 후 출력
          const filteredStatistics = {
            기쁨: response.data['기쁨'] || 0,
            불안: response.data['불안'] || 0,
            평온: response.data['평온'] || 0,
            슬픔: response.data['슬픔'] || 0,
            분노: response.data['분노'] || 0,
          };

          setStatistics(filteredStatistics); // 필터링된 통계 데이터를 상태에 저장
        })
        .catch(error => {
          console.error('Error fetching statistics data:', error);
          setError('통계 데이터를 가져오는 데 실패했습니다.');
        });
    } else {
      setError('No access token found');
    }
  }, []); // 컴포넌트가 처음 마운트될 때 한 번만 실행

  if (error) {
    return <div>{error}</div>; // 에러 메시지 출력
  }

  return (
    <div>
      <h1>Statistics Page</h1>
      {statistics ? (
        <div>
          <h2>감정 통계</h2>
          <ul>
            <li><strong>기쁨:</strong> {statistics['기쁨']}</li>
            <li><strong>분노:</strong> {statistics['분노']}</li>
            <li><strong>불안:</strong> {statistics['불안']}</li>
            <li><strong>슬픔:</strong> {statistics['슬픔']}</li>
            <li><strong>평온:</strong> {statistics['평온']}</li>
          </ul>
        </div>
      ) : (
        <p>Loading statistics...</p> // 데이터 로딩 중일 때 표시
      )}
    </div>
  );
}

export default StatisticsPage;
