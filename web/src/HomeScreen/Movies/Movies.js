import './Movies.css';
import React, { useState, useEffect  } from 'react';
import MovieRow from '../MovieRow/MovieRow.js';
import { useLocation } from 'react-router-dom';
import CreateCategory from '../../Admin/AdminActions/Category/CreateCategory/CreateCategory.js';
import { deleteCategory, getAllCategories, getCategoryById } from '../../Admin/AdminActions/Category/CategoryActions.js';

function Movies() {
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";
    const [showCategoryModal, setshowCategoryModal] = useState(false);
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const categoryIds = await getAllCategories();
                const categoryDetails = [];
            for (const id of categoryIds) {
                const category = await getCategoryById(id);
                if (category) {
                    categoryDetails.push(category);
                }
            }
            setCategories(categoryDetails);
            const unAttachedCategory = categoryDetails.find(cat => cat.name === "unAttached");
                if (unAttachedCategory && unAttachedCategory.movies.length === 0) {
                    const isDeleted = await deleteCategory(unAttachedCategory.id);
                    if (isDeleted) {
                        window.location.href = "/admin";
                    } else {
                        console.log('Failed to delete "unAttached" category.');
                    }
                }
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };
        fetchCategories();
    }, []);
    

    const handleAddCategory = () => {
        setshowCategoryModal(true);
    };

    const handleAddMovie = () => {
        window.location.href = "/admin/CreateMovie";
    };

    return (
        <div className="movies-container">
            {isAdminPage && (
                <>
                    <div className="add-category-container">
                        <button className="add-category-button" onClick={handleAddCategory}>
                            <i className="bi bi-plus-square"></i>
                        </button>
                        {showCategoryModal && <CreateCategory />}
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

            {/* {location === "/home" && <MovieRow category={{ name: "Watched", movies: [...] }} />} */}
            {categories.map((category) => {
                return <MovieRow key={category.id} category={category} />;
            })}            
        </div>
    );
}

export default Movies;