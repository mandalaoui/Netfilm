import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
// import './index.css';
import Connect from './Connect/Connect.js';
import Home from './HomeScreen/HomeScreen.js';
import WatchMovie from './WatchMovie/WatchMovie.js';
import Admin from './Admin/Admin.js';
import CreateMovie from './Admin/AdminActions/Movie/CreateMovie/CreateMovie.js';
import EditMovie from './Admin/AdminActions/Movie/EditMovie/EditMovie.js';
import { AuthGuard, AdminGuard } from './TokenGuard.js';
import 'bootstrap-icons/font/bootstrap-icons.css';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/" element={<Connect />} />
        <Route path="/login" element={<Connect />} />
        <Route path="/register" element={<Connect />} />
        <Route path="/home" element={<AuthGuard> <Home /> </AuthGuard>} />
        <Route path="/watch/:id" element={<AuthGuard> <WatchMovie /> </AuthGuard>} />
        <Route path="/admin" element={<AdminGuard> <Admin /> </AdminGuard>} />
        <Route path="/admin/CreateMovie" element={<AdminGuard> <CreateMovie /> </AdminGuard>} />
        <Route path="/admin/EditMovie" element={<AdminGuard> <EditMovie /> </AdminGuard>} />
      </Routes>
    </Router>
  </React.StrictMode>
);
