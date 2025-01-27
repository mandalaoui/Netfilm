const createMovie = (formData) => {
    return fetch("http://localhost:12345/api/movies/", {
        method: "POST",
        headers: {
            "userId": "6796506284c579985efe2882",
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

const getAllMovies = (userId) => {
    return fetch("http://localhost:12345/api/movies/", {
        method: "GET",
        headers: {
            "userId": userId,
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
        console.error("Error fetching movies:", error);
        return null;
    });
};

const getMovieById = (movieId, userId) => {
    return fetch(`http://localhost:12345/api/movies/${movieId}`, {
        method: "GET",
        headers: {
            "userId": userId,
        },
    })
    .then(response => {
        if (response.status === 200) {
            return response.json(); // מחזיר את המידע של הסרט הספציפי
        } else {
            return response.json().then(errorData => {
                const errorMessage = `${JSON.stringify(errorData)}`;
                alert(errorMessage);
                return null;
            });
        }
    })
    .catch(error => {
        console.error("Error fetching movie:", error);
        return null;
    });
};

const updateMovie = (movieId, userId, formData) => {
    return fetch(`http://localhost:12345/api/movies/${movieId}`, {
        method: "PUT",
        headers: {
            "userId": userId,
        },
        body: formData,
    })
    .then(response => {
        if (response.status === 204) {
            alert("Movie updated successfully!");
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
        console.error("Error updating movie:", error);
        return null;
    });
};

const deleteMovie = (movieId, userId) => {
    return fetch(`http://localhost:12345/api/movies/${movieId}`, {
        method: "DELETE",
        headers: {
            "userId": userId,
        },
    })
    .then(response => {
        if (response.status === 204) {
            alert("Movie deleted successfully!");
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
        console.error("Error deleting movie:", error);
        return false;
    });
};

export { createMovie, getAllMovies, getMovieById, updateMovie , deleteMovie }; 
