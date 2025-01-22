document.addEventListener("DOMContentLoaded", function () {
    const body = document.body;

    // Function to handle navigation to different pages
    function navigateToPage(page) {
        window.location.href = page; // Navigate to the specified page
    }

    // Adding the header
    const header = document.createElement("div");
    header.className = "header";
    header.textContent = "Netflix"; // Header text
    body.appendChild(header);

    // Creating the container
    const container = document.createElement("div");
    container.className = "container";
    body.appendChild(container);

    // Checking the page type (register or login)
    const pageType = body.dataset.pageType; // "register" or "login"

    // Main title
    const title = document.createElement("h1");
    title.textContent = pageType === "Login" ? "Login" : "Register"; // Set title based on page type
    container.appendChild(title);

    // Input fields
    const inputUserName = document.createElement("input");
    inputUserName.type = "text";
    inputUserName.placeholder = "Username"; // Placeholder for username input
    container.appendChild(inputUserName);

    const inputPassword = document.createElement("input");
    inputPassword.type = "password";
    inputPassword.placeholder = "Password"; // Placeholder for password input
    container.appendChild(inputPassword);

    if (pageType === "Register") {
        // More input fields for registeration
        const inputConfirmPassword = document.createElement("input");
        inputConfirmPassword.type = "password";
        inputConfirmPassword.placeholder = "Confirm Password";
        container.appendChild(inputConfirmPassword);

        const inputDisplayName = document.createElement("input");
        inputDisplayName.type = "text";
        inputDisplayName.placeholder = "Display Name";
        container.appendChild(inputDisplayName);

        const inputPictureContainer = document.createElement("div");
        inputPictureContainer.classList.add("input-group");

        const profilePicTitle = document.createElement("h6");
        profilePicTitle.textContent = "Profile Picture";
        inputPictureContainer.appendChild(profilePicTitle);

        // הוספת הערה על כך שהתמונה לא חובה
        const optionalText = document.createElement("small");
        optionalText.textContent = "*Optional";
        optionalText.classList.add("optional-text");
        inputPictureContainer.appendChild(optionalText);
        
        const inputPicture = document.createElement("input");
        inputPicture.type = "file";
        inputPicture.accept = "image/*";
        inputPictureContainer.appendChild(inputPicture);

        // הוספת אזור התמונה לקונטיינר הראשי
        container.appendChild(inputPictureContainer);
        
        // Register button
        const registerButton = document.createElement("button");
        registerButton.textContent = "Register"; // Register button text
        registerButton.addEventListener("click", function () {
            const userData = {
                username: inputUserName.value,
                password: inputPassword.value,
                // Add other data for registration if needed
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
                console.log("Registration successful:", data);
                //navigateToPage("Login.html");
            })
            .catch(error => console.error("Error during registration:", error));
        });
        container.appendChild(registerButton);

        // Login with existing user button
        const loginButton = document.createElement("button");
        loginButton.className = "secondary"; // Secondary button style
        loginButton.textContent = "Login with existing user"; // Text for login button
        loginButton.addEventListener("click", function () {
            navigateToPage("Login.html"); // Use the navigateToPage function to redirect
        });
        container.appendChild(loginButton);
    } else {
        // Login button
        const loginButton = document.createElement("button");
        loginButton.textContent = "Login"; // Login button text
        container.appendChild(loginButton);

        // Create new user button
        const registerButton = document.createElement("button");
        registerButton.className = "secondary"; // Secondary button style
        registerButton.textContent = "Create new user"; // Text for register button
        registerButton.addEventListener("click", function () {
            navigateToPage("Register.html"); // Use the navigateToPage function to redirect
        });
        container.appendChild(registerButton);
    }
});
