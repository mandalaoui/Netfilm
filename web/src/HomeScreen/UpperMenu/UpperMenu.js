import './UpperMenu.css';
import userIcon from '../../data/pictures/user.png';
import netflixIcon from '../../data/pictures/netflix.jpg';
import searchIcon from '../../data/pictures/search.png';
import { useState } from 'react';

function UpperMenu() {
    // const navigateToLogin = () => {
    //     window.location.href = "/login";
    // };

    const [showUserOptions, setShowUserOptions] = useState(false);
    const [showOptions, setShowOptions] = useState(false);

    const handleUserOptionsHover = () => {
        setShowUserOptions(true);
    };

    const handleUserOptionsLeave = () => {
        setShowUserOptions(false);
    };

    const handleOptionsHover = () => {
        setShowOptions(true);
    };

    const handleOptionsLeave = () => {
        setShowOptions(false);
    };

    return (
        <nav class="navbar border-body fixed-top full-width">
            <div className="container-fluid d-flex justify-content-between">
                <div className="d-flex me-auto">
                    <div
                        onMouseEnter={handleUserOptionsHover}
                        onMouseLeave={handleUserOptionsLeave}
                        className="position-relative"
                    >
                        <img src={userIcon} className="user-logo" />
                        {showUserOptions && (
                            <ul className="dropdown-user-menu">
                                <li><a className="dropdown-item" href="#">Info</a></li>
                                <li><a className="dropdown-item" href="#">Log Out</a></li>
                            </ul>
                        )}
                    </div>
                    <img src={searchIcon} className="search-logo" />
                </div>
                <div className="d-flex">
                    <div
                        onMouseEnter={handleOptionsHover}
                        onMouseLeave={handleOptionsLeave}
                        className="position-relative"
                    >
                        <button
                            className="btn btn-primary me-2"
                            type="button"
                        >
                            Options <i className="bi bi-arrow-down"></i>
                        </button>
                        {showOptions && (
                            <ul className="dropdown-menu">
                                <li><a className="dropdown-item" href="#">Home</a></li>
                                <li><a className="dropdown-item" href="#">Recommended</a></li>
                                <li><a className="dropdown-item" href="#">My Movies</a></li>
                            </ul>
                        )}
                    </div>
                    <img src={netflixIcon} className="netflix-logo" />
                </div>
            </div>
        </nav>
    );
}

export default UpperMenu;
