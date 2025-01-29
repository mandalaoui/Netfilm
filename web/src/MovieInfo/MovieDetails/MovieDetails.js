import './MovieDetails.css';
import React, { useState, useEffect } from 'react';
import { getCategoryById } from '../../Admin/AdminActions/Category/CategoryActions';
import { useNavigate } from 'react-router-dom';


function MovieDetails({ movie }) {
    const [movieCategories, setMovieCategories] = useState([]);
    const navigate = useNavigate();
    const baseApiUrl = "http://localhost:12345/api/";

    useEffect(() => {
        if (movie && movie.categories.length > 0) {
            const fetchCategories = async () => {
                try {
                    const categories = await Promise.all(
                        movie.categories.map((categoryId) => getCategoryById(categoryId))
                    );
                    setMovieCategories(categories);
                } catch (error) {
                    console.error("Error fetching categories:", error);
                }
            };
            fetchCategories();
        }
    }, [movie]);
    
    const handlePlayClick = () => {
        navigate(`/watchMovie/${movie._id}`); // Navigate to the watch page and pass the movie as state
    };
    const handlePlayTrailerClick = () => {
        navigate(`/watchTrailer/${movie._id}`);
    };

    return (
        <div className="movie-details-container">
            <div className="movie-details-info">
                <img className="movie-details-image" src={`${baseApiUrl}${movie?.image}`} alt={movie?.name} />
                <div className="movie-details">
                    <p><strong>Duration:</strong> {movie?.movie_time} </p>
                    <p><strong>Publication Year:</strong> {movie?.Publication_year}</p>
                    <p><strong>Age:</strong> {movie?.age ? `${movie.age}+` : "unlimites"}</p>
                    <p><strong>Description:</strong> {movie?.description}</p>
                    <p><strong>Categories:</strong> 
                        {movieCategories.length > 0 ? movieCategories.map((category, index) => (
                            <span key={index} className="movie-card-category">{category.name}</span>
                        )) : "No categories specified"}
                    </p>
                </div>
            </div>
            <div className="movie-details-actions">
                <button className="movie-details-play-trailer" onClick={handlePlayTrailerClick}>
                    Watch Trailer
                </button>
                <button className="movie-details-play-movie" onClick={handlePlayClick}>
                    Watch Movie
                </button>
            </div> 
        </div>
    );
}

export default MovieDetails;
