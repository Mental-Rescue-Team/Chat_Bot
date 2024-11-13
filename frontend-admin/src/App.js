// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './component/LoginPage';
import UserManagementPage from './component/UserManagementPage';
import ReportPage from './component/ReportPage';
import StatisticsPage from './component/StatisticsPage';
import Layout from './layout/Layout';

function App() {
  return (
    <Router>
      <Routes>
        {/* 로그인 페이지는 레이아웃 없이 표시 */}
        <Route path="/" element={<LoginPage />} />

        {/* 레이아웃을 사용하는 보호된 라우트 */}
        <Route path="/" element={<Layout />}>
          <Route path="user-management" element={<UserManagementPage />} />
          <Route path="statistics" element={<StatisticsPage />} />
          <Route path="report" element={<ReportPage />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
