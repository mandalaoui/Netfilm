const createMovie = (formData) => {
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
        console.error("Error fetching movies:", error);
        return null;
    });
};

const getMovieById = (movieId) => {
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
        console.error("Error fetching movie:", error);
        return null;
    });
};

const updateMovie = (movieId, formData) => {
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
        console.error("Error updating movie:", error);
        return null;
    });
};

const deleteMovie = (movieId) => {
    return fetch(`http://localhost:12345/api/movies/${movieId}`, {
        method: "DELETE",
        headers: {
            "userId": localStorage.getItem('userId'),
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
