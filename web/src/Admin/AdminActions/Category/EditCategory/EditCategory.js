import './EditCategory.css';
import React, { useState, useEffect } from 'react';
import { getMovieById } from '../../Movie/MovieActions.js';
import { updateCategory, getAllCategories, getCategoryById } from '../CategoryActions.js';


function EditCategory({ category }) {
    // State hooks to manage category data and UI elements
    const [categoryName, setCategoryName] = useState(category.name || '');
    const [isPromotedValue, setIsPromoted] = useState(category.isPromoted || false);
    const [movieNames, setMovieNames] = useState([]);
    const [allCategories, setAllCategories] = useState([]);
    const [allMovies, setAllMovies] = useState([]);
    const [selectedMovies, setSelectedMovies] = useState([]);
    
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
                console.log('Fetched all categories:', allCategoryDetails);

                setAllCategories(allCategoryDetails);
            } catch (error) {
                console.error("Error fetching all categories:", error);
            }
        };
        fetchAllCategories();
    }, []);

    useEffect(() => {
        // Fetch all movies associated with the categories
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
                console.error("Error fetching all categories:", error);
            }
        };
        if (allCategories.length > 0) {
            fetchAllMovies();
        }
    }, [allCategories]);

    useEffect(() => {
        // Fetch movie names using the IDs from the category
        const fetchMovieNames = async () => {
            try {
                const names = await Promise.all(
                    category.movies.map(async (movieId) => {
                        const movie = await getMovieById(movieId);
                        return movie?.name || 'Unknown Movie'; // Default to 'Unknown Movie' if the movie is not found
                    })
                );
                setMovieNames(names);
            } catch (error) {
                console.error('Error fetching movie names:', error);
            }
        };

        fetchMovieNames();
    }, [category.movies]);

    // Handle form submission to update the category
    const handleSubmit = () => {
        const categoryData = {
            name: categoryName,
            isPromoted: isPromotedValue,
            movies: selectedMovies,
        }
        updateCategory(category.id, categoryData)
            .then(isSuECess => {
                if (isSuECess) {
                    window.location.href = '/admin';
                }
            });
    };

    // Handle form submission to update the category
    const handleMovieChange = (e, moviesId) => {
        const updatedMovies = e.target.checked
            ? [...selectedMovies, moviesId]
            : selectedMovies.filter((id) => id !== moviesId); 

        setSelectedMovies(updatedMovies);
    };

    return (
        <div className="Edit-Category-modal-container">
            <div className="Edit-Category-modal-content">
                <h2>Edit Category</h2>
                <button className="EC-close-btn" onClick={() => window.location.href = '/admin'}>
                    <i className="bi bi-x-lg"></i>
                </button>
                <div className="category-layout">
                    <div className="current-category">
                        <h3>Current Category</h3>
                        <div className="input-group-ec">
                            <label>Name:</label>
                            <span>{category.name}</span>
                        </div>
                        <div className="input-group-ec">
                            <label>Promotion:</label>
                            <span>{category.isPromoted ? "Promoted" : "Not Promoted"}</span>
                        </div>
                        <div className="input-group-ec">
                            <label>Movies:</label>
                            <span>
                                {movieNames.length > 0 
                                    ? movieNames.join(", ") 
                                    : "Empty"}
                            </span>                        
                        </div>
                    </div>
                    <div className="new-category">
                        <h3>New Category</h3>
                        <div className="input-group-ec">
                            <label>Name:</label>
                            <input
                                type="text"
                                placeholder="Category Name"
                                value={categoryName}
                                onChange={(e) => setCategoryName(e.target.value)}
                            />
                        </div>
                        <div className="input-group-ec">
                            <label>Promotion:</label>
                            <div className="EC-checkbox">
                                <input
                                    type="checkbox"
                                    checked={isPromotedValue}
                                    onChange={(e) => setIsPromoted(e.target.checked)}
                                />
                            </div>
                        </div>
                        <div className="input-group-ec-update-movie">
                            <label>Movies:</label>
                            <div className="movie-update-category-list">
                                {allMovies.map((movie) => (
                                    <div key={movie._id} className="movie-update-category-item">
                                        <input
                                            type="checkbox"
                                            id={`movie-update-category-${movie._id}`}
                                            checked={selectedMovies.includes(movie._id)}
                                            onChange={(e) => handleMovieChange(e, movie._id)}
                                        />
                                        <label htmlFor={`movie-update-category-${movie._id}`} className="movies-update-category-label">
                                            <span className="movie-update-category-name">{movie.name}</span>
                                            <span className="movie-update-category-year">{movie.Publication_year}</span>
                                        </label>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
                <div className="edit-Category-btn">
                    <button onClick={handleSubmit}>Edit Category</button>
                </div>
            </div>
        </div>
    );
}

export default EditCategory;
