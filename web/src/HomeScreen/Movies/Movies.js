import './Movies.css';
import React from 'react';
import MovieRow from '../MovieRow/MovieRow.js';
import { useLocation } from 'react-router-dom';


function Movies() {
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";

    const handleAddCategory = () => {
        alert('Open modal to add a new category');
    };

    const handleAddMovie = () => {
        alert('Open modal to add a new movie');
    };

    return (
        <div className="movies-container">
            {isAdminPage && (
                <>
                    <div className="add-category-container">
                        <button className="add-category-button" onClick={handleAddCategory}>
                            <i className="bi bi-plus-square"></i>
                        </button>
                        <span className="add-category-text">Add Category</span>
                    </div>
                    <div className="add-movie-container">
                        <button className="add-movie-button" onClick={handleAddMovie}>
                            <i className="bi bi-plus-square"></i>
                        </button>
                        <span className="add-movie-text">Add Movie</span>
                    </div>
                </>
            )}

            {location === "/home" && <MovieRow category="Watched" />}
            <MovieRow category="Action" />
            <MovieRow category="Comedy" />
            <MovieRow category="Drama" />
            <MovieRow category="Family" />
        </div>
    );
}

export default Movies;