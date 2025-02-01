import './CreateCategory.css';
import React, { useState, useEffect } from 'react';
import { createCategory } from '../CategoryActions.js';
import { updateCategory, getAllCategories, getCategoryById } from '../CategoryActions.js';
import { getMovieById } from '../../Movie/MovieActions.js';
import { useNavigate } from 'react-router-dom';


function CreateCategory() {
    // State variables for category name and promotion status
    const [categoryName, setCategoryName] = useState('');
    const [isPromotedValue, setIsPromoted] = useState(false);
    const [allCategories, setAllCategories] = useState([]);
    const [allMovies, setAllMovies] = useState([]);
    const [selectedMovies, setSelectedMovies] = useState([]);
    const navigate = useNavigate();


    useEffect(() => {
        // Fetch all categories for selection
        const fetchAllCategories = async () => {
            try {
                const categories = await getAllCategories();
                const allCategoryDetails = await Promise.all(
                    categories.map(async (categoryId) => {
                        const fullCategory = await getCategoryById(categoryId);
                        return fullCategory;
                    })
                );
                setAllCategories(allCategoryDetails);
            } catch (error) {
                // console.error("Error fetching all categories:", error);
            }
        };
        fetchAllCategories();
    }, []);

    useEffect(() => {
        // Fetch all movies that belong to the fetched categories
        const fetchAllMovies = async () => {
            try {
                const allMoviesId = allCategories.flatMap((category) => category.movies || []);

                const allMoviesDetails = await Promise.all(
                    allMoviesId.map(async (movieId) => {
                        const fullMovie = await getMovieById(movieId);
                        return fullMovie;
                    })
                );
                const validMovies = allMoviesDetails.filter((movie) => movie !== null);
                const uniqueMovies = Array.from(new Map(validMovies.map(movie => [movie._id, movie])).values());
                setAllMovies(uniqueMovies);
            } catch (error) {
                // console.error("Error fetching all categories:", error);
            }
        };
        if (allCategories.length > 0) {
            fetchAllMovies();
        }
    }, [allCategories]);

    // Handle movie selection changes
    const handleMovieChange = (e, moviesId) => {
        const updatedMovies = e.target.checked
            ? [...selectedMovies, moviesId] 
            : selectedMovies.filter((id) => id !== moviesId); 

        setSelectedMovies(updatedMovies);
    };

    // Handle form submission
    const handleSubmit = async () => {
        try {
            // Check if "unAttached" category exists and remove selected movies from it
            const unAttachedCategory = allCategories.find(category => category.name === "unAttached");
            if (unAttachedCategory) {
                const moviesToRemove = unAttachedCategory.movies.filter(movieId => selectedMovies.includes(movieId));
                if (moviesToRemove.length > 0) {
                    const updatedMovies = unAttachedCategory.movies.filter(movieId => !moviesToRemove.includes(movieId));
                    await updateCategory(unAttachedCategory.id, {
                        ...unAttachedCategory,
                        movies: updatedMovies,
                    });
                }
            }    
        } catch (error) {
            // console.error("Error during category update:", error);
        }

        // Create new category with selected movies
        const name = categoryName;
        const isPromoted = isPromotedValue;
        const movies = selectedMovies;
        createCategory(name, isPromoted, movies)
            .then(isSuccess => {
                if (isSuccess) {
                    navigate('/admin');
                }
            });

    };

    

    return (
        <div className="Create-Category-modal-container">
            <div className="Create-Category-modal-content">
                <h2>Add Category</h2>
                <div className="input-group-CC">
                    <input
                        type="text"
                        placeholder="Category Name"
                        value={categoryName}
                        onChange={(e) => setCategoryName(e.target.value)}
                    />
                </div>
                <div className="input-group-CC">
                    <h7>Promotion</h7>
                    <div className="CC-checkbox">
                        <input
                            type="checkbox"
                            checked={isPromotedValue}
                            onChange={(e) => setIsPromoted(e.target.checked)}
                        />
                    </div>
                </div>
                <div className="input-group-CC-add-movie">
                    <h7>Movies</h7>
                    <div className="movies-in-category-list">
                        {allMovies.map((movie) => (
                            <div key={movie._id} className="movies-in-category-item">
                                <input
                                    type="checkbox"
                                    id={`movies-in-category-${movie._id}`}
                                    checked={selectedMovies.includes(movie._id)}
                                    onChange={(e) => handleMovieChange(e, movie._id)}
                                />
                                <label htmlFor={`movies-in-category-${movie._id}`} className="movies-in-category-label">
                                    <span className="movie-from-category-name">{movie.name}</span>
                                    <span className="movie-from-category-year">{movie.Publication_year}</span>
                                </label>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="Create-Category-btn">
                    <button onClick={handleSubmit}>Create Category</button>
                </div>
            </div>
        </div>
    );
}

export default CreateCategory;
