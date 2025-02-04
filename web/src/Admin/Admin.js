import './Admin.css';
import UpperMenu from '../HomeScreen/UpperMenu/UpperMenu.js';
import Movies from '../HomeScreen/Movies/Movies.js';
import Search from '../HomeScreen/Search/Search.js';
import { useLocation } from 'react-router-dom';
import { LocationProvider } from '../LocationContext.js';
import { useState } from 'react';

function Admin() {
    const location = useLocation();
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    return (
        <LocationProvider location={location}>
            <div className="home-container-admin">
                <UpperMenu searchQuery={searchQuery} onSearchChange={handleSearchChange} />
                <div className="admin-dashboard">
                    <div className="admin-welcome-message">
                        <h2>Welcome to the admin dashboard.</h2>
                        <p>Here you can manage your content:</p>
                        <ul>
                            <li>Add/Edit/Delete categories</li>
                            <li>Add/Edit/Delete movies</li>
                            <li>To log out, click the option in the upper menu.</li>
                        </ul>
                    </div>
                </div>
                {searchQuery === '' ? (
                    <Movies />
                ) : (
                    <Search query={searchQuery} />
                )}
                <div className="bottom-space"></div>
            </div>
        </LocationProvider>
    );
}

export default Admin;
