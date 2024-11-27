import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Doughnut, Bar } from 'react-chartjs-2';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, BarElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement, BarElement, CategoryScale, LinearScale);

function StatisticsPage() {
  const [statistics, setStatistics] = useState({});  // 서버에서 받은 통계 데이터를 저장
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

          const updatedStatistics = {
            기쁨: response.data['"기쁨"'] || 0,
            분노: response.data['"분노"'] || 0,
            불안: response.data['"불안"'] || 0,
            슬픔: response.data['"슬픔"'] || 0,
            평온: response.data['"평온"'] || 0,
          };

          setStatistics(updatedStatistics); // 통계 데이터를 상태에 저장

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

  const totalScore = Object.keys(statistics).length > 0
    ? Object.values(statistics).reduce((acc, val) => acc + val, 0)
    : 0;
  console.log(totalScore);


  const chartData = {
    labels: ['기쁨', '분노', '불안', '슬픔', '평온'],
    datasets: [
      {
        label: '감정 수',
        data: [
          statistics?.['기쁨'] || 0,
          statistics?.['분노'] || 0,
          statistics?.['불안'] || 0,
          statistics?.['슬픔'] || 0,
          statistics?.['평온'] || 0,
        ],
        backgroundColor: [
          '#FDFF27', // 기쁨: 노랑
          '#e74c3c', // 분노: 빨강
          '#e67e22', // 불안: 주황
          '#3498db', // 슬픔: 파랑
          '#2ecc71', // 평온: 초록
        ],
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
        position: 'bottom',  // 범례를 그래프 우측에 표시
        align: 'center',
        labels: {
          padding: 40, // 범례 항목 간 간격
        },
      },
    },
    cutout: '50%', // 도넛의 너비 조절 (숫자를 조정해 너비 변경 가능)
  };

  // 막대 그래프 데이터
  const barChartData = {
    labels: ['기쁨', '분노', '불안', '슬픔', '평온'],
    datasets: [
      {
        label: 'value of emotions',
        data: [
          statistics?.['기쁨'] || 0,
          statistics?.['분노'] || 0,
          statistics?.['불안'] || 0,
          statistics?.['슬픔'] || 0,
          statistics?.['평온'] || 0,
        ],
        backgroundColor: [
          '#FDFF27', // 기쁨: 노랑
          '#e74c3c', // 분노: 빨강
          '#e67e22', // 불안: 주황
          '#3498db', // 슬픔: 파랑
          '#2ecc71', // 평온: 초록
        ],
      },
    ],
  };

  // 막대 그래프 옵션
  const barChartOptions = {
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
    },
    responsive: true,
    scales: {
      x: {
        title: {
          display: true,
          text: '감정',
        },
      },
      y: {
        title: {
          display: true,
          text: '값',
        },
        beginAtZero: true,
      },
    },
  };

  const chartContainerStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: '50px',
    justifyContent: 'center',
  };

  const chartRowStyle = {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    gap: '50px',
  };

  return (
    <div style={chartContainerStyle}>
      <h1>감정 통계</h1>
      {statistics ? (
        <div>
          <p style={{fontSize: 20, textAlign: 'center'}}>감정 결과 총합: {totalScore}</p>
          <div style={chartRowStyle}>
            <div style={{ width: '500px', height: '500px' }}>
              <Doughnut data={chartData} options={chartOptions} />
            </div>
            <div style={{ width: '600px', height: '400px' }}>
              <Bar data={barChartData} options={barChartOptions} />
            </div>
          </div>
        </div>
      ) : (
        <p>Loading statistics...</p>
      )}
    </div>
  );
}

export default StatisticsPage;
