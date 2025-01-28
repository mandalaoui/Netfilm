import './CreateCategory.css';
import React, { useState, useEffect } from 'react';
import { createCategory } from '../CategoryActions.js';
// import moviesData  from '../../../../data/movies/movies.js';
import { updateCategory, getAllCategories, getCategoryById } from '../CategoryActions.js';
import { getMovieById } from '../../Movie/MovieActions.js';

function CreateCategory() {
    const [categoryName, setCategoryName] = useState('');
    const [isPromotedValue, setIsPromoted] = useState(false);

    const [allCategories, setAllCategories] = useState([]);
    // const [allMoviesById, setAllMoviesById] = useState([]);
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
        const fetchAllMovies = async () => {
            try {
                const allMoviesId = allCategories.flatMap((category) => category.movies || []);
                // setAllMoviesById(allMoviesId);                    

                const allMoviesDetails = await Promise.all(
                    allMoviesId.map(async (movieId) => {
                        const fullMovie = await getMovieById(movieId);
                        return fullMovie;
                    })
                );
                const validMovies = allMoviesDetails.filter((movie) => movie !== null);
                setAllMovies(validMovies);
            } catch (error) {
                console.error("Error fetching all categories:", error);
            }
        };
        if (allCategories.length > 0) {
            fetchAllMovies();
        }
    }, [allCategories]);

    const handleMovieChange = (e, moviesId) => {
        console.log('Category selected:', moviesId); // נוודא שהקטגוריה מתעדכנת

        const updatedMovies = e.target.checked
            ? [...selectedMovies, moviesId] // אם נבחר, נוסיף את הקטגוריה
            : selectedMovies.filter((id) => id !== moviesId); // אם בוטלה הבחירה, נסיר את הקטגוריה

        setSelectedMovies(updatedMovies);
        console.log('Updated selected categories:', updatedMovies); // נוודא שהקטגוריות מתעדכנות כראוי

    };

    const handleSubmit = async () => {

        try {
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
            console.error("Error during category update:", error);
        }

        const name = categoryName;
        const isPromoted = isPromotedValue;
        const movies = selectedMovies;
        // movies: selectedMovies,  // סרטים שנבחרו
        createCategory(name, isPromoted, movies)
            .then(isSuccess => {
                if (isSuccess) {
                    // document.querySelector('.Create-Category-modal-container').style.display = 'none'; 
                    window.location.href = '/admin';
                }
            });

    };

    

    return (
        <div className="Create-Category-modal-container">
            <div className="Create-Category-modal-content">
                <button className="CC-close-btn" onClick={() => window.location.href = '/admin'}>
                    <i className="bi bi-x-lg"></i>
                </button>
                <h2>Add Category</h2>
                <div className="input-group">
                    <input
                        type="text"
                        placeholder="Category Name"
                        value={categoryName}
                        onChange={(e) => setCategoryName(e.target.value)}
                    />
                </div>
                <div className="input-group">
                    <h7>Promotion</h7>
                    <div className="CC-checkbox">
                        <input
                            type="checkbox"
                            checked={isPromotedValue}
                            onChange={(e) => setIsPromoted(e.target.checked)}
                        />
                    </div>
                </div>
                <div className="input-group-add-movie">
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
