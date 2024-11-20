import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../App.css';

function ReportPage() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [userDetails, setUserDetails] = useState(null);

  // accessToken을 로컬 스토리지에서 가져오는 함수
  const getAccessToken = () => {
    const tokens = JSON.parse(localStorage.getItem('Tokens'));
    return tokens?.accessToken;
  };

  // 서버에서 사용자 이름 목록을 가져오는 함수
  useEffect(() => {
    const fetchUserNames = async () => {
      try {
        const response = await axios.get('/admin/names', {
          headers: {
            Authorization: `Bearer ${getAccessToken()}`,
          },
        });
        console.log(response.data);
        setUsers(response.data);
      } catch (error) {
        console.error("사용자 목록을 가져오는 중 오류 발생:", error);
      }
    };

    fetchUserNames();
  }, []);

  // 선택한 사용자의 상세 데이터를 가져오는 함수
  const fetchUserDetails = async (username) => {
    try {
      const response = await axios.get(`/admin/reports/${username}`, {
        headers: {
          Authorization: `Bearer ${getAccessToken()}`,
        },
      });
      setUserDetails(response.data);
      setSelectedUser(username);
    } catch (error) {
      console.error("사용자 상세 데이터를 가져오는 중 오류 발생:", error);
    }
  };

  return (
    <div className="report-page">
      <div className="user-list">
        {users.map((username, index) => (
          <button
            key={index}
            onClick={() => fetchUserDetails(username)}
            className="user-button"
          >
            {username}
          </button>
        ))}
      </div>

      <div className="user-details">
        {userDetails ? (
          <div>
            <h1>사용자 레포트 통계</h1>
            <pre>{JSON.stringify(userDetails, null, 2)}</pre>
          </div>
        ) : (
            <div>
                <h1>사용자 레포트 통계</h1>
                <p>사용자 이름을 클릭하여 상세 정보를 확인하세요.</p>
            </div>
          
        )}
      </div>
    </div>
  );
}

export default ReportPage;
