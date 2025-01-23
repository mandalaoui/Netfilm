import './MainMenu.css';
import { useLocationContext } from '../../LocationContext.js';
import { useEffect } from 'react';

function MainMenu() {
    const location = useLocationContext(); // Retrieve the current location from the context
    let loc = location.pathname;

    // Function to navigate between pages
    const navigateToPage = (page) => {
        window.location.href = page; // Navigate to the given page
    };

    useEffect(() => {
        function validateInput(input) {
            const errorElement = document.querySelector(`#${input.id}-error`);

            // Validation rules as an object
            const validationRules = {
                username: () => /^[a-zA-Z0-9]+$/.test(input.value.trim()) && input.value.trim().length >= 3, // Validates username format
                password: () => input.value.trim().length >= 6, // Validates password length
                confirm: () => input.value === document.querySelector('#password').value, // Confirms password match
                nickname: () => input.value.trim().length >= 3 // Validates nickname length
            };

            const isValid = validationRules[input.id]?.();
            if (isValid === false) {
                showError(errorElement); // Show error if validation fails
            } else {
                hideError(errorElement); // Hide error if validation passes
            }
        }

        const showError = (element) => {
            if (element) {
                element.style.display = 'block'; // Show the error message
            }
        };

        const hideError = (element) => {
            if (element) {
                element.style.display = 'none'; // Hide the error message
            }
        };

        // Add event listeners to inputs
        const inputs = document.querySelectorAll('input');
        inputs.forEach((input) => {
            input.addEventListener('input', () => validateInput(input)); // Listen for input changes
        });

        // Cleanup function to remove event listeners when component unmounts
        return () => {
            inputs.forEach((input) => {
                input.removeEventListener('input', () => validateInput(input)); // Remove event listeners
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
                body: JSON.stringify(userData) // Send user data to the server
            });

            let responseBody = null;
            try {
                responseBody = await response.json(); // Attempt to parse JSON response
            } catch (e) {
                console.warn("Failed to parse JSON:", e); // Warn if JSON parsing fails
            }

            if (response.ok) {
                alert("Registration successful!"); // Success case
                console.log("Response JSON:", responseBody);
                window.location.href = "/login"; // Redirect to login page after success
            } else if (response.status === 404) {
                alert("Username already exists. Please try another one."); // Error for duplicate username
            } else if (response.status === 400) {
                alert("One or more of the provided inputs are invalid."); // Error for invalid inputs
            } else {
                alert(`Unknown error occurred: HTTP ${response.status}`); // Generic error message
            }
        } catch (error) {
            console.error("Error during registration:", error); // Log error
            alert("Network error: Please try again later."); // Display network error message
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
            body: JSON.stringify(userData) // Send login data to server
        })
            .then(response => response.json()) // Parse JSON response
            .then(data => {
                console.log("Log In successful"); // Log success message
            })
            .catch(error => console.error("Error during login:", error)); // Log login error
    };
    return (
        <>
            {loc === "/" && (
                <div className="intro-container">
                    <h1>Movies, TV Shows, and more without limits</h1>
                    <h3>Want to start watching? Let's go!</h3>
                    <div className="intro-actions">
                        <button onClick={() => navigateToPage("/register")}>Create Account</button> {/* Button to navigate to register page */}
                    </div>
                </div>
            )}

            <div className={`container ${loc === "/" ? "hidden" : ""}`}>
                {loc !== "/" && (
                    <>
                        <h1>{loc === "/login" ? "Login" : "Register"}</h1>

                        <input type="text" id="username" placeholder="Username" />
                        <p className="error-message" id="username-error">Invalid username. Please enter a valid one.</p>
                        <input type="password" id="password" placeholder="Password" />
                        <p className="error-message" id="password-error">Password must contain at least 6 characters.</p>

                        {loc === "/register" && (
                            <>
                                <input type="password" id="confirm" placeholder="Confirm" />
                                <p className="error-message" id="confirm-error">Passwords do not match.</p>
                                <input type="text" id="nickname" placeholder="Nickname" />
                                <p className="error-message" id="nickname-error">Nickname cannot be less than 3 letters.</p>
                                <div className="input-group">
                                    <h6>Profile Picture</h6>
                                    <small className="optional-text">*optional</small> {/* Optional profile picture */}
                                    <input type="file" accept="image/*" /> {/* Profile picture upload */}
                                </div>
                                <button onClick={handleRegister}>Register</button> {/* Button to register user */}
                                <button
                                    className="secondary"
                                    onClick={() => navigateToPage("../login")}
                                >
                                    Login with existing user
                                </button> {/* Button to navigate to login page */}
                            </>
                        )}

                        {loc === "/login" && (
                            <>
                                <button onClick={handleLogin}>Login</button> {/* Button to handle login */}
                                <button
                                    className="secondary"
                                    onClick={() => navigateToPage("../register")}
                                >
                                    Create new user
                                </button> {/* Button to navigate to register page */}
                            </>
                        )}
                    </>
                )}
            </div>
        </>
    );
}

export default MainMenu;
