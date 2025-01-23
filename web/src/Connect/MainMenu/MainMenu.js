import './MainMenu.css';
import { useLocationContext } from '../../LocationContext.js';
import { useEffect } from 'react';

function MainMenu() {
    const location = useLocationContext(); // מקבלים את המיקום מה-context

    let loc = location.pathname;

    // Function to navigate between pages
    const navigateToPage = (page) => {
        window.location.href = page;
    };
    
    function validateInput(input) {
        const errorElement = document.querySelector(`#${input.id}-error`);
    
        // Validation rules as an object
        const validationRules = {
            username: () => /^[a-zA-Z0-9]+$/.test(input.value.trim()) && input.value.trim().length >= 3,
            password: () => input.value.trim().length >= 6,
            confirm: () => input.value === document.querySelector('#password').value,
            nickname: () => input.value.trim().length >= 3
        };
    
        const isValid = validationRules[input.id]?.();
        if (isValid === false) {
            showError(errorElement);
        } else {
            hideError(errorElement);
        }
    }
    
    const showError = (element) => {
        if (element) {
            element.style.display = 'block';
        }
    };

    const hideError = (element) => {
        if (element) {
            element.style.display = 'none';
        }
    };

    useEffect(() => {
        // Add event listeners to inputs
        const inputs = document.querySelectorAll('input');
        inputs.forEach((input) => {
            input.addEventListener('input', () => validateInput(input));
        });

        // Cleanup function to remove event listeners when component unmounts
        return () => {
            inputs.forEach((input) => {
                input.removeEventListener('input', () => validateInput(input));
            });
        };
    }, []);

    const handleRegister = async () => {
        const userData = {
            username: document.querySelector('input[placeholder="Username"]').value,
            password: document.querySelector('input[placeholder="Password"]').value,
            nickname: document.querySelector('input[placeholder="Nickname"]').value,
        };
    
        try {
            const response = await fetch("http://localhost:12345/api/users/", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(userData)
            });
    
            let responseBody = null;
            try {
                responseBody = await response.json(); // Attempt to parse JSON
            } catch (e) {
                console.warn("Failed to parse JSON:", e);
            }
    
            if (response.ok) {
                alert("Registration successful!"); // Success case
                console.log("Response JSON:", responseBody);
                window.location.href = "/login";
            } else if (response.status === 404) {
                alert("Username already exists. Please try another one.");
            } else if (response.status === 400) {
                alert("One or more of the provided inputs are invalid.");
            } else {
                alert(`Unknown error occurred: HTTP ${response.status}`);
            }
        } catch (error) {
            console.error("Error during registration:", error);
            alert("Network error: Please try again later.");
        }
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

                        <input type="text" id="username" placeholder="Username" />
                        <p class="error-message" id="username-error">Invalid username. Please enter a valid one.</p>
                        <input type="password" id="password" placeholder="Password" />
                        <p class="error-message" id="password-error">Password must contain at least 6 characters.</p>

                        {loc === "/register" && (
                            <>
                                <input type="password" id="confirm" placeholder="Confirm" />
                                <p class="error-message" id="confirm-error">Passwords do not match.</p>
                                <input type="text" id="nickname" placeholder="Nickname" />
                                <p class="error-message" id="nickname-error">Nickname cannot be less than 3 letters.</p>
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
