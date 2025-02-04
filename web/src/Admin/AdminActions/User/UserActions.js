// This function fetches a user by their ID from the server and handles the response, returning user data or an error.
const getUserById = (userId) => {
    if (!userId || userId === '') return null;
    return fetch(`http://localhost:12345/api/users/${userId}`, {
        method: "GET",
        headers: {
            "userId": localStorage.getItem('userId'),
        },
    })
    .then(response => {
        if (response.status === 200) {
            return response.json(); 
        } else {
            return response.json().then(errorData => {
                const errorMessage = `${JSON.stringify(errorData)}`;
                alert(errorMessage);
                return null;
            });
        }
    })
    .catch(error => {
        return null;
    });
};

export { getUserById }; 
