import './MainMenu.css';
import { useLocationContext } from '../../LocationContext.js';

function MainMenu() {
    const location = useLocationContext(); // מקבלים את המיקום מה-context

    let loc = location.pathname;

    // Function to navigate between pages
    const navigateToPage = (page) => {
        window.location.href = page;
    };

    const handleRegister = () => {
        const userData = {
            username: document.querySelector('input[placeholder="Username"]').value,
            password: document.querySelector('input[placeholder="Password"]').value,
            nickname: document.querySelector('input[placeholder="Nickname"]').value,
        };

        fetch("http://localhost:12345/api/users/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        })
            .then(response => response.json())
            .then(data => {
                console.log("Registration successful");
                // Navigate to login page after registration
            })
            .catch(error => console.error("Error during registration:", error));
    };

    const handleLogin = () => {
        const userData = {
            username: document.querySelector('input[placeholder="Username"]').value,
            password: document.querySelector('input[placeholder="Password"]').value,
        };

        fetch("http://localhost:12345/api/tokens/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        })
            .then(response => response.json())
            .then(data => {
                console.log("Log In successful");
            })
            .catch(error => console.error("Error during login:", error));
    };
    return (
        <>
            {loc === "/" && (
                <div className="intro-container">
                    <h1>Movies, TV Shows, and more without limits</h1>
                    <h3>Want to start watching? Let's go!</h3>
                    <div className="intro-actions">
                        <button onClick={() => navigateToPage("/register")}>Create Account</button>
                    </div>
                </div>
            )}

            <div className={`container ${loc === "/" ? "hidden" : ""}`}>
                {loc !== "/" && (
                    <>
                        <h1>{loc === "/login" ? "Login" : "Register"}</h1>

                        <input type="text" placeholder="Username" />
                        <input type="password" placeholder="Password" />

                        {loc === "/register" && (
                            <>
                                <input type="password" placeholder="Confirm Password" />
                                <input type="text" placeholder="Nickname" />
                                <div className="input-group">
                                    <h6>Profile Picture</h6>
                                    <small className="optional-text">*optional</small>
                                    <input type="file" accept="image/*" />
                                </div>
                                <button onClick={handleRegister}>Register</button>
                                <button
                                    className="secondary"
                                    onClick={() => navigateToPage("../login")}
                                >
                                    Login with existing user
                                </button>
                            </>
                        )}

                        {loc === "/login" && (
                            <>
                                <button onClick={handleLogin}>Login</button>
                                <button
                                    className="secondary"
                                    onClick={() => navigateToPage("../register")}
                                >
                                    Create new user
                                </button>
                            </>
                        )}
                    </>
                )}
            </div>
        </>
    );
}

export default MainMenu;
