import './MovieCard.css';
import { useLocation } from 'react-router-dom';
import React, { useEffect, useState } from 'react';
import { showConfirmationModal  } from '../../Admin/Verification/Verification.js'; 
import { getCategoryById } from '../../Admin/AdminActions/Category/CategoryActions.js';
import { deleteMovie } from '../../Admin/AdminActions/Movie/MovieActions.js';

function MovieCard({ movie }) {
    const [categories, setCategories] = useState([]);
    const location = useLocation();

    const navigateTo = (loc) => {
        window.location.href = loc;
    };
    // Function to handle play button click
    const handlePlayClick = () => {
        // console.log(`movie: ${movie.name}`);
        navigateTo(`../watch/${movie._id}`); // Navigate to the watch page and pass the movie as state
    };

    // const location = useLocation();

    const handleDeleteClick = async () => {
        const userConfirmed = await showConfirmationModal("movie",  movie.name, 'delete');
        if (userConfirmed) {
            try {
                const isDeleted = await deleteMovie(movie._id);
                if (isDeleted) {
                    navigateTo('/admin');
                    // console.log(`Movie "${movie.name}" deleted successfully.`);
                    // alert(`Movie "${movie.name}" has been deleted.`);
                } else {
                    alert(`Failed to delete movie "${movie.name}".`);
                }
            } catch (error) {
                console.error(`Error deleting movie "${movie.name}":`, error);
                alert(`An error occurred while deleting movie "${movie.name}".`);
            }
        } else {
            console.log('Delete action was canceled.');
        }
    };

    const handleEditClick = async () => {
        const userConfirmed = await showConfirmationModal("movie", movie.name, 'edit');
        if (userConfirmed) {
            navigateTo(`/admin/EditMovie?id=${movie._id}`);
        } else {
            console.log('Edit action was canceled.');
        }
    };

    const isAdminPage = location.pathname === '/admin'; 

    useEffect(() => {
        const fetchCategories = async () => {
            const fetchedCategories = await Promise.all(
                movie.categories.map(async (categoryId) => {
                    const category = await getCategoryById(categoryId);
                    return category.name; // Extract only the name
                })
            );
            setCategories(fetchedCategories);
        };

        fetchCategories();
    }, [movie.categories]);

    return (
        <div className="movie-card">
            <div className="movie-card-image">
            <img src={`http://localhost:12345/api/${movie.image}`} alt={movie.name} onClick={handlePlayClick}/>
            </div>
            <div className="movie-card-title">{movie.name}</div>
            <div className="movie-card-info">
                <div className="movie-card-controls">
                    <button className="movie-card-play-button" onClick={handlePlayClick}>▶ Play</button>
                    <span className="movie-card-movie_time">{movie.movie_time}</span>
                </div>
                
                <div className="movie-card-meta">
                    <p>{movie.Publication_year
                    } | {movie.age}</p>
                    <div className="movie-card-categories">
                    {categories.map((categoryName, index) => (
                            <span key={index} className="movie-card-category">{categoryName}</span>
                        ))}
                    </div>
                </div>
                                
                {isAdminPage && (
                    <>
                        <button className="delete-movie-card-button" onClick={handleDeleteClick}>
                            <i className="bi bi-trash"></i>
                        </button>
                        <button className="edit-movie-card-button" onClick={handleEditClick}>
                            <i className="bi bi-pencil-square"></i>
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}

export default MovieCard;
