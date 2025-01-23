import './MovieCard.css';
import React from 'react';

function MovieCard({ movie }) {
    return (
        <div className="movie-card">
            <div className="movie-image">
                <img src={movie.image} alt={movie.title} />
            </div>
            <div className="movie-title">{movie.title}</div>
            <div className="movie-info">
                <div className="movie-controls">
                    <button className="play-button">▶ Play</button>
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
