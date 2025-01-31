import './Recommendations.css';
import React, { useState, useEffect } from 'react';
import MovieCard from '../../HomeScreen/MovieCard/MovieCard';
import { getRecommendations } from '../../Functions/UserFunctions';

function Recommendations({ movie }) {
    const [isLoading, setIsLoading] = useState(false);  // State for loading status
    const [error, setError] = useState(null);  // State for error handling
    const [recommendedMovies, setRecommendedMovies] = useState([]);  // State for storing recommended movies

    useEffect(() => {
        // Start loading and clear previous errors
        setIsLoading(true);
        setError(null);
        const fetchRecommendations = async () => {
            try {
                console.log("lets go movie: ", movie);
                console.log("trying to grt recommendauins for: ", movie._id);
                // Check if movie ID is valid
                if (!movie._id || movie._id === "") {
                    throw new Error("Invalid movie ID.");
                }
                // Fetch recommended movies based on current movie's ID
                const recommendations = await getRecommendations(movie._id);
                console.log("fetching recommendations now: ", recommendations)
   
                // If recommendations are found, update state, otherwise show error message
                if (recommendations && recommendations.length > 0) {
                    setRecommendedMovies(recommendations); 
                } else {
                    setError("Failed to fetch recommendations. Please try again.");
            }
            } catch (err) {
                setError("Failed to fetch recommendations. Please try again.");
            } finally {
                setIsLoading(false);
            }
        }
        fetchRecommendations();
    }, [movie]);

    return (
        <div className="Recommendations-container">
            <h2> Recommendations: </h2>
            <div className="recommended-movies" >
                {isLoading && <p>Loading...</p>}
                {error && console.log(error)}
                {recommendedMovies && recommendedMovies.length > 0 ? (
                    recommendedMovies.map((movie) => (
                        <MovieCard key={movie._id} movie={movie} />
                    ))
                ) : (
                    <p>No Recommended Movies found</p>
                )}
            </div>
        </div>
    );
}

export default Recommendations;
