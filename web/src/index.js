import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './index.css';
import Connect2 from './Connect/Connect2.js';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/" element={<Connect2 />} />
        <Route path="/login" element={<Connect2 />} />
        <Route path="/register" element={<Connect2 />} />
      </Routes>
    </Router>
  </React.StrictMode>
);
