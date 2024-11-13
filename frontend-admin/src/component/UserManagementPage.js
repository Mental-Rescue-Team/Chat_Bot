import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../App.css';

function UserManagementPage() {
  const [users, setUsers] = useState([]);  // 유저 데이터를 저장할 상태
  const [error, setError] = useState(null);  // 에러 상태

  useEffect(() => {
    // localStorage에서 accessToken 가져오기
    const storedTokens = JSON.parse(localStorage.getItem('Tokens'));
    if (storedTokens && storedTokens.accessToken) {
      const token = storedTokens.accessToken;

      // 서버의 엔드포인트 URL을 입력하세요
      axios.get('/admin', {
        headers: {
          Authorization: `Bearer ${token}` // accessToken을 Authorization 헤더에 추가
        }
      })
        .then(response => {
          console.log('Received data from server:', response.data);
          // 서버에서 받은 data를 상태에 저장
          setUsers(response.data.data); // data 필드를 users 상태에 저장
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          setError('서버 데이터를 가져오는 데 실패했습니다.');
        });
    } else {
      setError('No access token found');
    }
  }, []); // 컴포넌트가 처음 마운트될 때 한 번만 실행

  const handleDelete = (member_no) => {
    const storedTokens = JSON.parse(localStorage.getItem('Tokens'));
    if (storedTokens && storedTokens.accessToken) {
      const token = storedTokens.accessToken;

      axios.delete(`/admin/${member_no}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
        .then(() => {
          setUsers(prevUsers => prevUsers.filter(user => user.member_no !== member_no));
        })
        .catch(error => {
          console.error('Error deleting user:', error);
          setError('유저를 삭제하는 데 실패했습니다.');
        });
    } else {
      setError('No access token found');
    }
  };

  return (
    <div className="user-management">
      <h1>User Management</h1>
      
      {error && <p>{error}</p>} {/* 에러 메시지 출력 */}

      <table>
        <thead>
          <tr>
            <th>Member No</th>
            <th>Username</th>
            <th>E-mail</th>
            <th>Register Date</th>
            <th>Role</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map(user => (
              <tr key={user.member_no}>
                <td>{user.member_no}</td>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>{new Date(user.registerDate).toLocaleString()}</td> {/* 날짜 포맷 변경 */}
                <td>{user.role}</td>
                <td>
                  <button 
                  className="delete-button"
                  onClick={() => handleDelete(user.member_no)}
                  >
                    Delete
                    </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6">No users found</td> {/* 유저가 없을 경우 메시지 표시 */}
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default UserManagementPage;
