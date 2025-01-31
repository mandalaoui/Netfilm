import React, { createContext, useContext, useState } from 'react';

const GlobalContext = createContext();

export const GlobalProvider = ({ children }) => {
  const [fileUrl, setFileUrl] = useState('http://localhost:12345/api/');

  return (
    <GlobalContext.Provider value={{ fileUrl, setFileUrl }}>
      {children}
    </GlobalContext.Provider>
  );
};

export const useGlobalContext = () => {
  return useContext(GlobalContext);
};
