import './MovieCard.css';
import { useLocation } from 'react-router-dom';
import React, { useEffect, useState } from 'react';
import { showConfirmationModal } from '../../Admin/Verification/Verification.js';
import { getCategoryById } from '../../Admin/AdminActions/Category/CategoryActions.js';
import { deleteMovie } from '../../Admin/AdminActions/Movie/MovieActions.js';
import { useGlobalContext } from '../../GlobalContext.js';
import { useNavigate } from 'react-router-dom';

// MovieCard component receives a 'movie' object as a prop
function MovieCard({ movie }) {
    const [categories, setCategories] = useState([]);
    const location = useLocation();
    const { fileUrl } = useGlobalContext();
    const navigate = useNavigate();

    // Function to handle play button click
    const handlePlayClick = () => {
        navigate(`../watchMovie/${movie._id}`);
    };

    // Function to handle the info button click
    const handleInfoClick = () => {
        navigate(`../movie/${movie._id}`);
    };


    // Function to handle delete movie action
    const handleDeleteClick = async () => {
        const userConfirmed = await showConfirmationModal("movie", movie.name, 'delete');
        if (userConfirmed) {
            try {
                const isDeleted = await deleteMovie(movie._id);
                if (isDeleted) {
                    navigate('/admin');
                } else {
                    alert(`Failed to delete movie "${movie.name}".`);
                }
            } catch (error) {
                // console.error(`Error deleting movie "${movie.name}":`, error);
                alert(`An error occurred while deleting movie "${movie.name}".`);
            }
        } else {
            // console.log('Delete action was canceled.');
        }
    };

    // Function to handle edit movie action
    const handleEditClick = async () => {
        const userConfirmed = await showConfirmationModal("movie", movie.name, 'edit');
        if (userConfirmed) {
            navigate(`/admin/EditMovie?id=${movie._id}`);
        } else {
            // console.log('Edit action was canceled.');
        }
    };

    const isAdminPage = location.pathname === '/admin';

    // Fetch categories associated with the movie on mount
    useEffect(() => {
        if (movie) {
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
        }
    }, [movie]);

    return (
        <div className="movie-card">
            <div className="movie-card-image" >
                <img src={`${fileUrl}${movie.image}`} alt={movie.name} />
            </div>
            <div className="movie-card-title" onClick={handleInfoClick} >{movie.name}</div>
            <button className="movie-card-play-button" onClick={handlePlayClick}>▶ Play</button>
            <div className="movie-card-info" onClick={handleInfoClick}>
                <div className="movie-card-meta">
                    <p>{movie.Publication_year
                    } | {movie.age} | {movie.movie_time}</p>
                    <div className="movie-card-categories">
                        {categories.map((categoryName, index) => (
                            <span key={index} className="movie-card-category">{categoryName}</span>
                        ))}
                    </div>
                </div>
            </div>
            <div className="admin-card-options">
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
