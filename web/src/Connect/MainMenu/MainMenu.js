import './MainMenu.css';
import { useLocationContext } from '../../LocationContext.js';
import { useEffect } from 'react';
import { useGlobalContext } from '../../GlobalContext.js';
import { useNavigate } from 'react-router-dom';

function MainMenu() {
    const location = useLocationContext(); // Retrieve the current location from the context
    let loc = location.pathname;
    const { fileUrl } = useGlobalContext(); 
    const navigate = useNavigate();

    useEffect(() => {
        function validateInput(input) {
            const errorElement = document.querySelector(`#${input.id}-error`);

            // Validation rules as an object
            const validationRules = {
                username: () => /^[a-zA-Z0-9]+$/.test(input.value.trim()) && input.value.trim().length >= 3, // Validates username format
                password: () => input.value.trim().length >= 6, // Validates password length
                confirm: () => input.value === document.querySelector('#password').value, // Confirms password match
                nickname: () => input.value.trim().length >= 1 // Validates nickname length
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

    const validateUserData = (userData) => {
        if (!userData.username) {
            alert("Please enter username");
            return false;
        }
        if (!userData.password) {
            alert("Please enter password");
            return false;
        }
    
        const repeatedPassword = document.querySelector('input[placeholder="Confirm"]').value;
        if (userData.password !== repeatedPassword) {
            alert("Passwords do not match.");
            return false;
        }
    
        if (!userData.nickname) {
            alert("Please enter nickname");
            return false;
        }
        
        return true;
    };


    const handleRegister = async () => {
        const userData = {
            username: document.querySelector('input[placeholder="Username"]').value,
            password: document.querySelector('input[placeholder="Password"]').value,
            nickname: document.querySelector('input[placeholder="Nickname"]').value,
        }
        if (!validateUserData(userData))
            return;

        const imageInput = document.querySelector('input[type="file"]');
        const imageFile = imageInput.files[0];

        const formData = new FormData();
        formData.append('username', userData.username);
        formData.append('password', userData.password);
        formData.append('nickname', userData.nickname);
        if (imageFile != null)
            formData.append('photo', imageFile);
        try {
            const response = await fetch(`${fileUrl}users/`, {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                alert("Registration successful!");
                navigate("/login");
            } else {
                const errorResponse = await response.json();
                if (response.status === 400) {
                    alert(`Invalid input: ${errorResponse.errors?.join(', ') || 'Unknown error'}`);
                } else if (response.status === 404) {
                    alert("Username already exists. Please try another one.");
                } else {
                    alert(`Error: ${response.status} ${errorResponse.errors?.join(', ') || 'Unknown error'}`);
                }
            }
        } catch (error) {
            alert("Network error: Please try again later.");
        }
    };

    const handleLogin = async () => {
        const userData = {
            username: document.querySelector('input[placeholder="Username"]').value,
            password: document.querySelector('input[placeholder="Password"]').value,
        };
        if (!userData.username) return alert("Please enter username");
        if (!userData.password) return alert("Please enter password");

        const errorElement = document.getElementById('login-error');

        try {
            const response = await fetch(`${fileUrl}tokens/`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(userData) // Send login data to server
            });
            if (response.ok) {
                const data = await response.json(); // Parse response
                const { token } = data;
                
                localStorage.setItem('authToken', token);
                const decodedToken = JSON.parse(atob(token.split('.')[1])); // Decode the JWT token
                const userId = decodedToken.id;
                const isAdmin = decodedToken.isAdmin;

                localStorage.setItem('userId', userId);
                localStorage.setItem('isAdmin', isAdmin);

                navigate('../home');
            } else if (response.status === 400 || response.status === 404) {
                errorElement.textContent = "Username and/or password are incorrect.";
                errorElement.style.display = "block";
            } else {
                errorElement.textContent = "An unexpected error occurred. Please try again.";
                errorElement.style.display = "block";
            }
        } catch (error) {
            errorElement.textContent = "Network error. Please check your connection.";
            errorElement.style.display = "block";
        }
    };
    return (
        <>
            {loc === "/" && (
                <div className="intro-container">
                    <h1>Movies, TV Shows, and more without limits</h1>
                    <h3>Want to start watching? Let's go!</h3>
                    <div className="intro-actions">
                        <button onClick={() => navigate("/register")}>Create Account</button> {/* Button to navigate to register page */}
                    </div>
                </div>
            )}

            <div className={`menu-container ${loc === "/" ? "hidden" : ""}`}>
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
                                <p className="error-message" id="nickname-error">Please enter nickname.</p>
                                <div className="profile-picture">
                                    <div className="profile-picture-text">
                                        <h6>Profile Picture</h6>
                                        <small className="optional-text">*optional</small> {/* Optional profile picture */}
                                    </div>
                                    <input type="file" accept="image/*" /> {/* Profile picture upload */}
                                </div>
                                <button onClick={handleRegister}>Register</button> {/* Button to register user */}
                                <button
                                    className="secondary"
                                    onClick={() => navigate("../login")}
                                >
                                    Login with existing user
                                </button> {/* Button to navigate to login page */}
                            </>
                        )}

                        {loc === "/login" && (
                            <>
                                <p id="login-error" className="login-error-message" style={{ display: "none", color: "red" }}></p>
                                <button onClick={handleLogin}>Login</button> {/* Button to handle login */}
                                <button
                                    className="secondary"
                                    onClick={() => navigate("../register")}
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
