import './UpperMenu.css';
import userIcon from '../../data/pictures/user.png';
import netflixIcon from '../../data/pictures/netflix.jpg';
import searchIcon from '../../data/pictures/search.png';
import { useState, useEffect  } from 'react';
import { useLocation } from 'react-router-dom';
import { getUserById } from '../../Admin/AdminActions/User/UserActions';

function UpperMenu({ searchQuery, onSearchChange }) {
    const navigateTo = (loc) => {
        window.location.href = loc;
    };

    const [showUserOptions, setShowUserOptions] = useState(false);
    const [showOptions, setShowOptions] = useState(false);
    const [searchActive, setSearchActive] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [userImage, setUserImage] = useState(null);
    const location = useLocation();
    const [isAdminScreen, setIsAdminScreen] = useState(false);

    useEffect(() => {
        const adminStatus = localStorage.getItem('isAdmin');
        if (adminStatus === 'true') {
            setIsAdmin(true);
        }
    }, []);

    useEffect(() => {
        if (location.pathname === '/admin') {
          setIsAdminScreen(true);
        } else {
          setIsAdminScreen(false);
        }
      }, [location]);  

      useEffect(() => {
        const fetchUserData = async () => {
            const userId = localStorage.getItem('userId');
            if (userId) {
                const user = await getUserById(userId);
                if (user && user.photo) {
                    setUserImage(`http://localhost:12345/api/${user.photo}`);
                }
            }
        };
        fetchUserData();
    }, []);
    
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

    const handleLogOut = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('isAdmin');
        localStorage.removeItem('userId');
        navigateTo('../');
    };

    const openSearch = () => {
        setSearchActive(true);
    };

    const closeSearch = () => {
        onSearchChange({ target: { value: '' } });
        setSearchActive(false);
    };

    return (
        <nav className="navbar border-body fixed-top full-width">
            <div className="container-fluid d-flex justify-content-between">
                <div className="d-flex me-auto">
                    <img src={netflixIcon} className="netflix-logo" alt="Netflix Logo"></img>
                    <div
                        onMouseEnter={handleOptionsHover}
                        onMouseLeave={handleOptionsLeave}
                        className="position-relative"
                    >
                        <button
                            className="btn btn-primary me-2"
                            type="button"
                        >
                            <i className="bi bi-arrow-down"></i> Options
                        </button>
                        {showOptions && (
                            <ul className="dropdown-menu">
                                <li><a className="dropdown-item" href='/'>Home</a></li>
                                <li><a className="dropdown-item" href='/'>Recommended</a></li>
                                <li><a className="dropdown-item" href='/'>My Movies</a></li>
                            </ul>
                        )}
                    </div>
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
                                <li><a className="dropdown-item" href='/'>Info</a></li>
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

