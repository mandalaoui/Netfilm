import './UpperMenu.css';
import { useLocationContext } from '../../LocationContext.js';

function UpperMenu() {
    const location = useLocationContext(); // Retrieve the current location from the context
    let loc = location.pathname;
    // Function to navigate to the login page
    const navigateToLogin = () => {
        window.location.href = "/login";
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-dark transparent-bg fixed-top full-width">
            <div className="container-fluid d-flex justify-content-between align-items-center">
                <div className="header ms-2">NETFILM</div>

                {loc === "/" && (
                    <div className="d-flex me-2">
                        <button className="btn btn-secondary me-2" type="button">Language</button>
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
