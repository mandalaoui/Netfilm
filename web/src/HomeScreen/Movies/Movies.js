import './Movies.css';
import React, { useState, useEffect  } from 'react';
import MovieRow from '../MovieRow/MovieRow.js';
import { useLocation } from 'react-router-dom';
import CreateCategory from '../../Admin/AdminActions/Category/CreateCategory/CreateCategory.js';
import { deleteCategory, getAllCategories, getCategoryById } from '../../Admin/AdminActions/Category/CategoryActions.js';
import { getUserById } from '../../Admin/AdminActions/User/UserActions.js';
import { useNavigate } from 'react-router-dom';

function Movies() {
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";
    const isHomePage = location.pathname === "/home";
    const isMoviesByPage = location.pathname === "/moviesByCategories";
    const [showCategoryModal, setshowCategoryModal] = useState(false);
    const [categories, setCategories] = useState([]);
    const [watchList, setwatchList] = useState([]);
    const navigate = useNavigate();

    // Fetch categories from the server when the component mounts
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                // Fetch category IDs from the server
                const categoryIds = await getAllCategories();
                const categoryDetails = [];
                // Loop over category IDs to get each category details
                for (const id of categoryIds) {
                const category = await getCategoryById(id);
                if (category) {
                    categoryDetails.push(category);
                }
            }
            setCategories(categoryDetails);
                // Check if there's a category named "unAttached" and if it has no movies
                const unAttachedCategory = categoryDetails.find(cat => cat.name === "unAttached");
                if (unAttachedCategory && unAttachedCategory.movies.length === 0) {
                    // If "unAttached" category is empty, delete it
                    const isDeleted = await deleteCategory(unAttachedCategory.id);
                    if (isDeleted) {
                        navigate("/admin");
                    } else {
                        // console.log('Failed to delete "unAttached" category.');
                    }
                }
            } catch (error) {
                // console.error("Error fetching categories:", error);
            }
        };
        fetchCategories();
    }, [categories, categories.Movies, navigate]);
    
    // Fetch user data (specifically watched movies) when the component mounts
    useEffect(() => {
        const fetchUserData = async () => {
            const userId = localStorage.getItem('userId');
            if (userId) {
                const user = await getUserById(userId);
                if (user && user.watchedMovies) {
                    const uniqueMovies = [...new Set(user.watchedMovies)];
                    setwatchList(uniqueMovies);
                }
            }
        };
        fetchUserData();
    }, []);

    // Handler to show the category modal
    const handleAddCategory = () => {
        setshowCategoryModal(true);
    };

    // Handler to navigate to the "CreateMovie" page
    const handleAddMovie = () => {
        navigate("/admin/CreateMovie");
    };

    return (
        <div className="movies-container">
            {isAdminPage && (
                <>
                    <div className="add-category-container">
                        <button className="add-category-button" onClick={handleAddCategory}>
                            <i className="bi bi-plus-square"></i>
                        </button>
                        {showCategoryModal && (
                                <div className="modal-content">
                                    <button className="modal-close-button" onClick={() => setshowCategoryModal(false)}>X</button>
                                    <CreateCategory />
                                </div>
                        )}
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
            
            <MovieRow key={1} category={{ name: "Watched", movies: watchList, isPromoted: true }} />
            {categories.map((category) => {
                if (isAdminPage){
                    if(category.name === "unAttached" && category.movies.length === 0) return null;
                    return <MovieRow key={category.id} category={category} />;
                }
                if (isMoviesByPage && category.name !== "unAttached")
                    return <MovieRow key={category.id} category={category} />;
                if (isHomePage && category.isPromoted && category.name !== "unAttached")
                    return <MovieRow key={category.id} category={category} />;
                return null;
            })}            
        </div>
    );
}

export default Movies;