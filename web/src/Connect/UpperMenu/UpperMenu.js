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
            <div className="container-fluid d-flex justify-content-between">
                <div className="d-flex me-auto">
                    {loc === "/" && (
                        <>
                            <button className="btn btn-danger me-2" type="button" onClick={navigateToLogin}>
                                Login
                            </button>
                            <button className="btn btn-secondary" type="button">Language</button>
                        </>
                    )}
                </div>

                <div className="header">NETFLIX</div>
            </div>
        </nav>
    );
}

export default UpperMenu;
