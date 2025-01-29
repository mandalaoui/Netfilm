import './Recommendations.css';
import React, { useState, useEffect } from 'react';
import MovieCard from '../../HomeScreen/MovieCard/MovieCard';
import { getRecommendations } from '../../Functions/UserFunctions';

function Recommendations({ movie }) {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [recommendedMovies, setRecommendedMovies] = useState([]);

    useEffect(() => {
        setIsLoading(true);
        setError(null);
        const fetchRecommendations = async () => {
            try {
                const recommendations = await getRecommendations(movie._id);

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
    }, [movie._id]);

    return (
        <div className="Recommendations-container">
            <h2> Recommendations: </h2>
            <div className="recommended-movies" >
                {isLoading && <p>Loading...</p>}
                {error && <p>{error}</p>}
                {recommendedMovies.length > 0 ? (
                    recommendedMovies.map((movie) => (
                        <MovieCard key={movie._id} movie={movie} />
                    ))
                ) : (
                    !error && <p>No Recommended Movies found</p>
                )}
            </div>
        </div>
    );
}

export default Recommendations;
