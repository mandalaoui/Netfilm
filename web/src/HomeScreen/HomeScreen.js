import './HomeScreen.css';
import UpperMenu from './UpperMenu/UpperMenu.js';
import HomeScreenMovie from './HomeScreenMovie/HomeScreenMovie.js';
import Movies from './Movies/Movies.js';
import Search from './Search/Search.js';
import { useLocation } from 'react-router-dom';
import { LocationProvider } from '../LocationContext.js';
import { useState } from 'react';

function HomeScreen() {
    const location = useLocation(); // Get current location of the app
    const [searchQuery, setSearchQuery] = useState(''); // State to hold search query

    // Function to handle changes in search input
    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    return (
        <LocationProvider location={location}>
            <div className="home-container">
                <UpperMenu searchQuery={searchQuery} onSearchChange={handleSearchChange} />
                {searchQuery === '' ? (
                    <>
                        <HomeScreenMovie />
                        <Movies />
                    </>
                ) : (
                    <Search query={searchQuery} />
                )}
                <div className="bottom-space"></div>
            </div>
        </LocationProvider>
    );
}

export default HomeScreen;
