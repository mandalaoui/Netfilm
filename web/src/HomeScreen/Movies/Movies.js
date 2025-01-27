import './Movies.css';
import React, { useState, useEffect  } from 'react';
import MovieRow from '../MovieRow/MovieRow.js';
import { useLocation } from 'react-router-dom';
import CreateCategory from '../../Admin/AdminActions/Category/CreateCategory/CreateCategory.js';
// import CreateMovie from '../../Admin/AdminActions/Movie/CreateMovie/CreateMovie.js';
import { getAllCategories, getCategoryById } from '../../Admin/AdminActions/Category/CategoryActions.js';


function Movies() {
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";
    const [showCategoryModal, setshowCategoryModal] = useState(false);
    // const [showMovieModal, setShowMovieModal] = useState(false);
    const [categories, setCategories] = useState([]);
    // const categories = ["Action", "Comedy", "Drama", "Family", "New", "SpiderMan"];
    useEffect(() => {
        const userId = "67964782c8b5942c5f45547f";
        const fetchCategories = async () => {
            try {
                const categoryIds = await getAllCategories(userId);
                const categoryDetails = [];
            for (const id of categoryIds) {
                const category = await getCategoryById(id, userId);
                if (category) {
                    categoryDetails.push(category);
                }
            }
            setCategories(categoryDetails);
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
        // setShowMovieModal(true);
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
            {categories.map((category) => (
                <MovieRow key={category.id} category={category} />
            ))}
        </div>
    );
}

export default Movies;