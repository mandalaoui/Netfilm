const getUserById = (userId) => {
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
        console.error("Error fetching user:", error);
        return null;
    });
};

export { getUserById }; 
