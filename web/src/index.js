import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
// import './index.css';
import Connect from './Connect/Connect.js';
import Home from './HomeScreen/HomeScreen.js';
import WatchMovie from './WatchMovie/WatchMovie.js';
import 'bootstrap-icons/font/bootstrap-icons.css';





const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/" element={<Connect />} />
        <Route path="/login" element={<Connect />} />
        <Route path="/register" element={<Connect />} />
        <Route path="/Home" element={<Home />} />
        <Route path="/Watch/:id" element={<WatchMovie />} />
      </Routes>
    </Router>
  </React.StrictMode>
);
