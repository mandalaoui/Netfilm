import './MovieDetails.css';
import React, { useState, useEffect } from 'react';
import { getCategoryById } from '../../Admin/AdminActions/Category/CategoryActions';
import { useNavigate } from 'react-router-dom';
import { useGlobalContext } from '../../GlobalContext';

function MovieDetails({ movie }) {
    // State to store movie categories
    const [movieCategories, setMovieCategories] = useState([]);
    const navigate = useNavigate(); // Navigation function to change routes
    const { fileUrl } = useGlobalContext();

    useEffect(() => {
        // Check if the movie has categories and fetch them
        if (movie && movie.categories.length > 0) {
            const fetchCategories = async () => {
                try {
                    // Fetch categories based on movie categories
                    const categories = await Promise.all(
                        movie.categories.map( async (categoryId) => {
                            const category = await getCategoryById(categoryId);
                            return category.name;
                        })
                    );
                    setMovieCategories(categories);
                } catch (error) {
                    console.error("Error fetching categories:", error);
                }
            };
            fetchCategories();
        }
    }, [movie]); 

    // Handle play button click to navigate to watch page    
    const handlePlayClick = () => {
        navigate(`/watchMovie/${movie._id}`); // Navigate to the watch page and pass the movie as state
    };
    // Handle play trailer button click to navigate to trailer page
    const handlePlayTrailerClick = () => {
        navigate(`/watchTrailer/${movie._id}`);
    };

    return (
        <div className="movie-details-container">
            <div className="movie-details-info">
                <img className="movie-details-image" src={`${fileUrl}${movie?.image}`} alt={movie?.name} />
                <div className="movie-details">
                    <p><strong>Duration:</strong> {movie?.movie_time} </p>
                    <p><strong>Publication Year:</strong> {movie?.Publication_year}</p>
                    <p><strong>Age:</strong> {movie?.age ? `${movie.age}+` : "unlimites"}</p>
                    <p><strong>Description:</strong> {movie?.description}</p>
                    <p><strong>Categories:</strong> 
                        {movieCategories.length > 0 ? movieCategories.map((category, index) => (
                            <span key={index} className="movie-details-category">{category}</span>
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
