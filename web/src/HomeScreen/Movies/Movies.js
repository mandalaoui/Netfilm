import './Movies.css';
import React, { useState, useEffect  } from 'react';
import MovieRow from '../MovieRow/MovieRow.js';
import { useLocation } from 'react-router-dom';
import CreateCategory from '../../Admin/AdminActions/Category/CreateCategory/CreateCategory.js';
import { getAllCategories } from '../../Admin/AdminActions/AdminActions.js';



function Movies() {
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";
    const [showModal, setShowModal] = useState(false);
    const [categories, setCategories] = useState([]);

    // const categories = ["Action", "Comedy", "Drama", "Family", "New", "SpiderMan"];

    useEffect(() => {
        const userId = "6793f41b8221f4dda02b7e63";
        getAllCategories(userId).then(fetchedCategories => {
            setCategories(fetchedCategories);
        });
    }, []);

    const handleAddCategory = () => {
        setShowModal(true);
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
                        {showModal && <CreateCategory />}
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
            {categories.map((category) => (
                <MovieRow key={category} category={category} />
            ))}
        </div>
    );
}

export default Movies;