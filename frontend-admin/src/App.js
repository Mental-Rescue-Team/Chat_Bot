import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './component/LoginPage';
import UserManagementPage from './component/UserManagementPage';
import ReportPage from './component/ReportPage';
import StatisticsPage from './component/StatisticsPage';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/user-management" element={<UserManagementPage />} />
          <Route path="/report" element={<ReportPage />} />
          <Route path="/statistics" element={<StatisticsPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;