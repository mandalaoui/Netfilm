import React, { createContext, useContext } from 'react';

const LocationContext = createContext();

export function LocationProvider({ children, location }) {
  return (
    <LocationContext.Provider value={location}>
      {children}
    </LocationContext.Provider>
  );
}

export function useLocationContext() {
  return useContext(LocationContext);
}
