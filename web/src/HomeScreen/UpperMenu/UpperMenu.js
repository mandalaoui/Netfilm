import './UpperMenu.css';
import userIcon from '../../icons/user.png';
import netfilmIcon from '../../icons/NETFILM.png';
import searchIcon from '../../icons/search.png';
import React, { useContext, useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { getUserById } from '../../Admin/AdminActions/User/UserActions';
import UserInfo from '../UserInfo/UserInfo.js';
import { ThemeContext } from "../../ThemeContext";
import { useGlobalContext } from '../../GlobalContext.js';

// Main component function for the upper menu
function UpperMenu({ searchQuery, onSearchChange }) {
    const navigate = useNavigate();
    const { fileUrl } = useGlobalContext(); 

    // Function to handle navigation
    const navigateTo = (loc) => {
        window.location.href = loc;
    };

    // State hooks for different features
    const [showUserOptions, setShowUserOptions] = useState(false);
    const [searchActive, setSearchActive] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [userImage, setUserImage] = useState(null);
    const location = useLocation();
    const [isAdminScreen, setIsAdminScreen] = useState(false);
    const [showInfoModal, setShowInfoModal] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);
    const { isLightMode, setIsLightMode } = useContext(ThemeContext);

    // Check if the user is an admin based on localStorage
    useEffect(() => {
        const adminStatus = localStorage.getItem('isAdmin');
        if (adminStatus === 'true') {
            setIsAdmin(true);
        }
    }, []);

    // Check if the current route is the admin screen
    useEffect(() => {
        if (location.pathname === '/admin') {
            setIsAdminScreen(true);
        } else {
            setIsAdminScreen(false);
        }
    }, [location]);

    // Fetch user data and set the image URL if available
    useEffect(() => {
        const fetchUserData = async () => {
            const userId = localStorage.getItem('userId');
            if (userId) {
                const user = await getUserById(userId);
                setCurrentUser(user);
                if (user && user.photo) {
                    setUserImage(`${fileUrl}${user.photo}`);
                }
            }
        };
        fetchUserData();
    }, [fileUrl]);

    // Show user options when hovering over the user icon
    const handleUserOptionsHover = () => {
        setShowUserOptions(true);
    };

    // Hide user options when mouse leaves the user icon
    const handleUserOptionsLeave = () => {
        setShowUserOptions(false);
    };

    // Navigate to the home page
    const handleHome = () => {
        navigate('/home');
    };

    // Log out the user
    const handleLogOut = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('isAdmin');
        localStorage.removeItem('userId');
        navigateTo('../');
    };

    // Open the search input field
    const openSearch = () => {
        setSearchActive(true);
    };

    // Close the search input field and clear the query
    const closeSearch = () => {
        onSearchChange({ target: { value: '' } });
        setSearchActive(false);
    };

    // Show user info modal
    const handleInfo = () => {
        setShowInfoModal(true);
    }
    // Close user info modal
    const handleCloseInfo = () => {
        setShowInfoModal(false);
    };

    // Toggle light/dark mode
    const handleLightMode = () => {
        setIsLightMode(prev => !prev);
      };
      

    return (
        <nav className="navbar border-body fixed-top full-width">
            <div className="container-fluid d-flex justify-content-between">
                <div className="d-flex me-auto">
                    <img src={netfilmIcon} className="netfilm-logo" alt="Netfilm Logo"></img>
                    <button className="btn btn-primary me-2" type="button" onClick={handleHome}>
                        <i className="bi bi-house"></i> Home
                    </button>
                </div>
                <div className="d-flex search-container">
                    <img src={searchIcon} className="search-logo" alt="Search Logo" onClick={openSearch}></img>
                    {searchActive && (
                        <div className="search-input-wrapper">
                            <input
                                type="text"
                                className="search-input"
                                placeholder="Search..."
                                value={searchQuery}
                                onChange={onSearchChange}
                                autoFocus
                            />
                            <button className="clear-search" onClick={closeSearch}>X</button>
                        </div>
                    )}
                </div>
                <div className="d-flex">
                    <div
                        onMouseEnter={handleUserOptionsHover}
                        onMouseLeave={handleUserOptionsLeave}
                        className="position-relative"
                    >
                        <img
                            src={userImage || userIcon}
                            className="user-logo"
                            alt="User logo"
                        />
                        {showUserOptions && (
                            <ul className="dropdown-user-menu">
                                <li>
                                    <span className="dropdown-item" onClick={handleInfo} role="button" tabIndex="0">
                                        Info
                                    </span>
                                    <span className="dropdown-item" onClick={handleLightMode} role="button" tabIndex="0">
                                    {isLightMode ? 'Dark Mode' : 'Light Mode'}
                                    </span>
                                </li>
                                {showInfoModal && (
                                    <UserInfo user={currentUser} onClose={handleCloseInfo} />
                                )}
                                {isAdmin && !isAdminScreen && (
                                    <li><a className="dropdown-item" href="/admin" >Admin Screen</a></li>
                                )}
                                {isAdmin && isAdminScreen && (
                                    <li><a className="dropdown-item" href="/home" >User Screen</a></li>
                                )}
                                <li><a className="dropdown-item" href='/' onClick={handleLogOut}>Log Out</a></li>
                            </ul>
                        )}
                    </div>
                </div>
            </div>
        </nav >
    );
}

export default UpperMenu;

