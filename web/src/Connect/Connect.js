import './Connect.css';
import UpperMenu from './UpperMenu/UpperMenu.js';
import MainMenu from './MainMenu/MainMenu.js';
import { useLocation } from 'react-router-dom'; 
import { LocationProvider } from '../LocationContext.js';

function Connect2() {
    const location = useLocation();
    
    return (
        <LocationProvider location={location}>
            <div class="home-body">
                <UpperMenu />
                <MainMenu />
            </div>
        </LocationProvider>
    );
}

export default Connect2;
