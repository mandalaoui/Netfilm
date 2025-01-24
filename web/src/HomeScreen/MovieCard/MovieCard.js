import './MovieCard.css';
import React from 'react';

function MovieCard({ movie }) {
    const navigateTo = (loc) => {
        window.location.href = loc;
    };
    // Function to handle play button click
    const handlePlayClick = () => {
        navigateTo(`../watch/${movie.id}`); // Navigate to the watch page with the movie ID
    };

    return (
        <div className="movie-card">
            <div className="movie-image">
                <img src={movie.image} alt={movie.title} onClick={handlePlayClick}/>
            </div>
            <div className="movie-title">{movie.title}</div>
            <div className="movie-info">
                <div className="movie-controls">
                    <button className="play-button" onClick={handlePlayClick}>▶ Play</button>
                    <span className="movie-duration">{movie.duration}</span>
                </div>
                
                <div className="movie-meta">
                    <p>{movie.releaseYear} | {movie.ageRating}</p>
                    <div className="movie-categories">
                        {movie.categories.map((category, index) => (
                            <span key={index} className="movie-category">{category}</span>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MovieCard;
