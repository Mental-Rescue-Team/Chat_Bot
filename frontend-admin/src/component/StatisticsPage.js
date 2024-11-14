import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Doughnut } from 'react-chartjs-2';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale);

function StatisticsPage() {
  const [statistics, setStatistics] = useState(null);  // 서버에서 받은 통계 데이터를 저장
  const [error, setError] = useState(null);  // 에러 상태
//   const [totalScore, setTotalScore] = useState(5);

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
          const filteredStatistics = {};

          for (const [key, value] of Object.entries(response.data)) {
            if (key.includes('"response":"불안"')) {
              filteredStatistics['불안'] = value;
            } else if (key.includes('"response":"평온"')) {
              filteredStatistics['평온'] = value;
            } else if (key.includes('"response":"기쁨"')) {
              filteredStatistics['기쁨'] = value;
            } else if (key.includes('"response":"슬픔"')) {
                filteredStatistics['슬픔'] = value;
            } else if (key.includes('"response":"분노"')) {
                filteredStatistics['분노'] = value;
            }
          }

          setStatistics(filteredStatistics); // 필터링된 통계 데이터를 상태에 저장

          // 총점 계산
        //   const total = Object.values(filteredStatistics).reduce((acc, val) => acc + val, 0);
        //   setTotalScore(total); // 계산된 총점을 상태에 저장
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

//   const totalScore = statistics 
//     ? Object.values(statistics).reduce((acc, val) => acc + val, 0) 
//     : 0;

  const chartData = {
    labels: ['기쁨', '분노', '불안', '슬픔', '평온'],
    datasets: [
      {
        label: '감정 통계',
        data: [
          statistics?.['기쁨'] || 0,
          statistics?.['분노'] || 0,
          statistics?.['불안'] || 0,
          statistics?.['슬픔'] || 0,
          statistics?.['평온'] || 0,
        ],
        backgroundColor: ['#9b59b6', '#8e44ad', '#7d3c98', '#6c3483', '#5b2c6f'], // 감정별 색상
      },
    ],
  };

  const chartOptions = {
    plugins: {
      tooltip: {
        enabled: true
      },
      legend: {
        display: true,
        position: 'right',  // 범례를 그래프 우측에 표시
        align: 'center',
      },
      // 총점을 표시하는 사용자 정의 플러그인
    //   customCanvasBackgroundColor: {
    //     id: 'centerText',
    //     beforeDraw(chart) {
    //       const { ctx, width, height } = chart;
    //       const fontSize = (height / 120).toFixed(2); // 폰트 크기 조정
    //       ctx.restore();
    //       ctx.font = `${fontSize}em sans-serif`;
    //       ctx.textBaseline = 'middle';
    //       ctx.textAlign = 'center';

    //       const text = `${totalScore}`;
    //       const textX = width / 2;
    //       const textY = height / 2;

    //       ctx.fillText(text, textX, textY);
    //       ctx.save();
    //     }
    //   }
    },
    cutout: '65%', // 도넛의 너비 조절 (숫자를 조정해 너비 변경 가능)
  };

  // ChartJS.register({
  //   id: 'centerText',
  //   beforeDraw: chartOptions.plugins.customCanvasBackgroundColor.beforeDraw,
  // });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: '100vh', justifyContent: 'center' }}>
      <h1>Emotion Statistics</h1>
      {statistics ? (
        <div style={{ textAlign: 'center' }}>
          <div style={{ width: '400px', height: '400px' }}>
            <Doughnut data={chartData} options={chartOptions} />
          </div>
        </div>
      ) : (
        <p>Loading statistics...</p>
      )}
    </div>
  );
}

export default StatisticsPage;
