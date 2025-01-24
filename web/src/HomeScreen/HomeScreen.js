import './HomeScreen.css';
import UpperMenu from './UpperMenu/UpperMenu.js';
import Recommended from './Recommended/Recommended.js';
import Movies from './Movies/Movies.js';
import Search from './Search/Search.js';
import { useLocation } from 'react-router-dom';
import { LocationProvider } from '../LocationContext.js';
import { useState } from 'react';

function HomeScreen() {
    const location = useLocation();
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    return (
        <LocationProvider location={location}>
            <div className="home-container">
                <UpperMenu searchQuery={searchQuery} onSearchChange={handleSearchChange} />
                {searchQuery === '' ? (
                    <>
                        <Recommended />
                        <Movies />
                    </>
                ) : (
                    <Search query={searchQuery} />
                )}
            </div>
        </LocationProvider>
    );
}

export default HomeScreen;
