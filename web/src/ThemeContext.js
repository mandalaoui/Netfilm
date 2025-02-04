import { createContext, useState, useEffect } from 'react';

export const ThemeContext = createContext();

export const ThemeProvider = ({ children }) => {
  const [isLightMode, setIsLightMode] = useState(
    localStorage.getItem("theme") === "light"
  );

  useEffect(() => {
    if (isLightMode) {
      document.documentElement.classList.add("light-mode");
      document.documentElement.classList.remove("dark-mode"); // הסרת dark-mode
      localStorage.setItem("theme", "light");
    } else {
      document.documentElement.classList.add("dark-mode");
      document.documentElement.classList.remove("light-mode"); // הסרת light-mode
      localStorage.setItem("theme", "dark");
    }
  }, [isLightMode]);

  return (
    <ThemeContext.Provider value={{ isLightMode, setIsLightMode }}>
      {children}
    </ThemeContext.Provider>
  );
};
