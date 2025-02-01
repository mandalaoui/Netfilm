import './UpperMenu.css';
import { useLocationContext } from '../../LocationContext.js';
import { useNavigate } from 'react-router-dom';

function UpperMenu() {
    const location = useLocationContext(); // Retrieve the current location from the context
    let loc = location.pathname;
    const navigate = useNavigate();

    // Function to navigate to the login page
    const navigateToLogin = () => {
        navigate("/login");
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-dark transparent-bg fixed-top full-width">
            <div className="container-fluid d-flex justify-content-between align-items-center">
                <div className="header ms-2">NETFILM</div>

                {loc === "/" && (
                    <div className="d-flex me-2">
                        <button className="btn btn-danger" type="button" onClick={navigateToLogin}>
                            Login
                        </button>
                    </div>
                )}
            </div>
        </nav>
    );
}

export default UpperMenu;
