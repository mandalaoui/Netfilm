// Fetch recommendations for a given movie
const getRecommendations = async (movieId) => {
    return fetch (`http://localhost:12345/api/movies/${movieId}/recommend/`, {
            method: "GET",
            headers: {
                "userId": localStorage.getItem('userId'),
            },
        })
        .then((response) => {
            if (response.status === 200) {
                return response.json(); 
            } else {
                alert(`ID is not available, try another one - ${response.status}`);
                throw new Error(`Error: ${response.status} - ${response.statusText}`);
            }
        })
        .then(data => {
            if (!data) return []; 
            if (Array.isArray(data)) {
                return data;
            } else {
                return [];
            }
        })
        .catch((error) => {
            console.error("Error fetching recommendations:", error);
            return []; 
        });
};

// Add a movie to the user's watchlist
const addMovieToWatchlist = async (movieId) => {
    try {
        const response = await fetch(`http://localhost:12345/api/movies/${movieId}/recommend/`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "userId": localStorage.getItem('userId'),
            },
            body: JSON.stringify({
                userId: localStorage.getItem('userId'),
            }),
        });

        if (response.status === 201) {
            alert("Movie added to watchlist!");
            return true;
        } else {
            const errorData = await response.json();
            const errorMessage = `${JSON.stringify(errorData)}`;
            alert(errorMessage);
            return false;
        }
    } catch (error) {
        console.error("Error adding movie to watchlist:", error);
        return false;
    }
};

export { getRecommendations, addMovieToWatchlist };
