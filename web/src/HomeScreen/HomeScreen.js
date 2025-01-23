import './HomeScreen.css';
import UpperMenu from './UpperMenu/UpperMenu.js';
import Recommended from './Recommended/Recommended.js';
import Movies from './Movies/Movies.js';
import { useLocation } from 'react-router-dom';
import { LocationProvider } from '../LocationContext.js';

function HomeScreen() {
    const location = useLocation();

    return (
        <LocationProvider location={location}>
            <div className="home-container">
                    <UpperMenu />
                    <Recommended />
                    <Movies />
            </div>
        </LocationProvider>
    );
}

export default HomeScreen;
