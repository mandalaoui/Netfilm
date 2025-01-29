import './UpperMenu.css';
import userIcon from '../../icons/user.png';
import netfilmIcon from '../../icons/NETFILM.png';
import searchIcon from '../../icons/search.png';
import { useState, useEffect  } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { getUserById } from '../../Admin/AdminActions/User/UserActions';

function UpperMenu({ searchQuery, onSearchChange }) {
    const navigate = useNavigate();
    const navigateTo = (loc) => {
        window.location.href = loc;
    };

    const [showUserOptions, setShowUserOptions] = useState(false);
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

    const handleHome = () => {
        navigate('/home');
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

