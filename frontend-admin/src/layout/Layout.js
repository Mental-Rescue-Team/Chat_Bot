// src/components/Layout.js
import React, { useState } from 'react';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import './Layout.css'; // 스타일을 위한 CSS 파일

function Layout() {
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const handleLogout = () => {
    // 로그아웃 로직 (예: 토큰 삭제)
    localStorage.removeItem('Tokens');
    navigate('/');
  };

  return (
    <div className="layout">
      <button className="menu-button" onClick={toggleMenu}>
        ☰
      </button>

      {menuOpen && (
        <div className="sidebar">
          <ul>
            <li>
              <Link to="/user-management">Member Management</Link>
            </li>
            <li>
              <Link to="/statistics">Statistics Logs</Link>
            </li>
            <li>
              <Link to="/report">Report Logs</Link>
            </li>
          </ul>
          <button className="logout-button" onClick={handleLogout}>
            LOG OUT
          </button>
        </div>
      )}

      <div className="content">
        <Outlet />
      </div>
    </div>
  );
}

export default Layout;
