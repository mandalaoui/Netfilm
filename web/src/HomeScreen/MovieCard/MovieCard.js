import './MovieCard.css';
import { useLocation } from 'react-router-dom';
import React from 'react';
import { showConfirmationModal  } from '../../Admin/Verification/Verification.js'; 


function MovieCard({ movie }) {
    const navigateTo = (loc) => {
        window.location.href = loc;
    };
    // Function to handle play button click
    const handlePlayClick = () => {
        navigateTo(`../watch/${movie._id}`); // Navigate to the watch page with the movie ID
    };

    const location = useLocation();

    const handleDeleteClick = async () => {
        const userConfirmed = await showConfirmationModal("movie",  movie.title, 'delete');
        if (userConfirmed) {
            // Perform the delete action here (e.g., call an API or update state)
            console.log(`Movie ${movie.title} deleted.`);
        } else {
            console.log('Delete action was canceled.');
        }
    };

    const handleEditClick = async () => {
        const userConfirmed = await showConfirmationModal("movie", movie.title, 'edit');
        if (userConfirmed) {
            // Perform the edit action here (e.g., navigate to an edit page or open an editor)
            console.log(`Editing movie ${movie.title}.`);
        } else {
            console.log('Edit action was canceled.');
        }
    };

    const isAdminPage = location.pathname === '/admin'; 

    return (
        <div className="movie-card">
            <div className="movie-image">
            <img src={`http://localhost:12345/api/${movie.image}`} alt={movie.title} onClick={handlePlayClick}/>
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
                                
                {isAdminPage && (
                    <>
                        <button className="delete-movie-button" onClick={handleDeleteClick}>
                            <i className="bi bi-trash"></i>
                        </button>
                        <button className="edit-movie-button" onClick={handleEditClick}>
                            <i className="bi bi-pencil-square"></i>
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}

export default MovieCard;
