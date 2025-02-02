// This function creates a new movie by sending form data to the server.
const createMovie = (formData) => {
    if (!formData || formData === '') return null;
    return fetch("http://localhost:12345/api/movies/", {
        method: "POST",
        headers: {
            "userId": localStorage.getItem('userId'),
        },
        body: formData,
    })
    .then(response => {
        if (response.status === 201) {
            alert("Movie created successfully!");
            return true;    
        } else {
            return response.json().then(errorData => {
            const errorMessage = `${JSON.stringify(errorData)}`;
            alert(errorMessage);
            });
        }
    })
    .catch(error => {
        return false;
    }); 
};

// This function fetches all movies from the server.
const getAllMovies = () => {
    return fetch("http://localhost:12345/api/movies/", {
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

// This function fetches a single movie by its ID.
const getMovieById = (movieId) => {
    if(!movieId || movieId === '') return null;
    return fetch(`http://localhost:12345/api/movies/${movieId}`, {
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

// This function updates an existing movie by its ID with the provided form data.
const updateMovie = (movieId, formData) => {
    if(!movieId || movieId === '') return null;
    return fetch(`http://localhost:12345/api/movies/${movieId}`, {
        method: "PUT",
        headers: {
            "userId": localStorage.getItem('userId'),
        },
        body: formData,
    })
    .then(response => {
        if (response.status === 204) {
            alert("Movie updated successfully!");
            return true; 
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

// This function deletes a movie by its ID.
const deleteMovie = (movieId) => {
    if(!movieId | movieId === '') return null;
    return fetch(`http://localhost:12345/api/movies/${movieId}`, {
        method: "DELETE",
        headers: {
            "userId": localStorage.getItem('userId'),
        },
    })
    .then(response => {
        if (response.status === 204) {
            // alert("Movie deleted successfully!");
            return true; 
        } else {
            return response.json().then(errorData => {
                const errorMessage = `${JSON.stringify(errorData)}`;
                alert(errorMessage);
                return false;
            });
        }
    })
    .catch(error => {
        return false;
    });
};

export { createMovie, getAllMovies, getMovieById, updateMovie , deleteMovie }; 
