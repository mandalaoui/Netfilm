import './EditMovie.css';
import UpperMenu from '../../../../HomeScreen/UpperMenu/UpperMenu.js';
import React, { useState, useEffect } from 'react';
import { updateMovie } from '../MovieActions.js';
// import { useLocation } from 'react-router-dom';
import { getCategoryById, getAllCategories } from '../../Category/CategoryActions.js';
import { getMovieById } from '../MovieActions.js';


function EditMovie() {
    // const location = useLocation();
    // const queryParams = new URLSearchParams(location.search);
    // const specificCategory = queryParams.get('specificCategory');
    const [currentMovie, setCurrentMovie] = useState(''); // State to store the current movie
    const [movieCategories, setMovieCategories] = useState([]); // State for storing movie categories
    const [allCategories, setAllCategories] = useState([]);
    const urlParams = new URLSearchParams(window.location.search);
    const movieId = urlParams.get('id');
    const [selectedCategories, setSelectedCategories] = useState([]);


    useEffect(() => {
        // Fetch all categories for selection
        const fetchAllCategories = async () => {
            try {
                const categories = await getAllCategories();
                const allCategoryDetails = await Promise.all(
                    categories.map(async (category) => {
                        const fullCategory = await getCategoryById(category); // משיג את האובייקט המלא
                        return fullCategory; // מחזיר את האובייקט המלא
                    })
                );
                console.log(allCategoryDetails);

                setAllCategories(allCategoryDetails);
                // console.log(categories);
            } catch (error) {
                console.error("Error fetching all categories:", error);
            }
        };

        fetchAllCategories();
    }, []);

    useEffect(() => {
        // Fetch movie details when movieId is available
        const fetchMovie = async () => {
            if (movieId) {
                try {
                    const movie = await getMovieById(movieId);
                    setCurrentMovie(movie);
                    // console.log(currentMovie.name);
                } catch (error) {
                    console.error("Error fetching movie:", error);
                }
            }
        };

        fetchMovie(); // Call the async function to fetch the movie details
    }, [movieId]);

    useEffect(() => {
        if (currentMovie && currentMovie.categories.length > 0) {
            const fetchCategories = async () => {
                try {
                    const categories = await Promise.all(
                        currentMovie.categories.map((categoryId) => getCategoryById(categoryId))
                    );
                    setMovieCategories(categories);
                } catch (error) {
                    console.error("Error fetching categories:", error);
                }
            };

            fetchCategories();
        }
    }, [currentMovie]);

    useEffect(() => {
        function validateInput(input) {
            const errorElement = document.querySelector(`#${input.id}-error`);

            // Validation rules as an object
            const validationRules = {
                movieName: () => input.value.trim().length >= 2, // Movie name must be at least 3 characters long
                movieTime: () => /^([0-9]{1,2}):([0-5][0-9])$/.test(input.value.trim()), // Ensure it's in the format "HH:MM"
                publicationYear: () => {
                    const currentYear = new Date().getFullYear();
                    return input.value > 0 && input.value <= currentYear; // Validate year
                },
                age: () => input.value % 2 === 0 && input.value > 0, // Age must be even and greater than 0
                trailer: () => input.files.length > 0,
                video: () => input.files.length > 0,
                image: () => input.files.length > 0
            };

            const isValid = validationRules[input.id]?.();
            if (isValid === false) {
                showError(errorElement); // Show error if validation fails
            } else {
                hideError(errorElement); // Hide error if validation passes
            }
        }

        const showError = (element) => {
            if (element) {
                element.style.display = 'block'; // Show the error message
            }
        };

        const hideError = (element) => {
            if (element) {
                element.style.display = 'none'; // Hide the error message
            }
        };

        // Add event listeners to inputs
        const inputs = document.querySelectorAll('input');
        inputs.forEach((input) => {
            input.addEventListener('input', () => validateInput(input)); // Listen for input changes
        });

        // Cleanup function to remove event listeners when component unmounts
        return () => {
            inputs.forEach((input) => {
                input.removeEventListener('input', () => validateInput(input)); // Remove event listeners
            });
        };
    }, []);

    const handleReturn = () => {
        window.location.href = '/admin';
    };
    
    const handleSubmit = () => {
        const renameFilePath = (filePath, newName) => {
            const ext = filePath.split('.').pop();
            return `C:\\fakepath\\${newName}.${ext}`;
        };
        const videoFile = document.querySelector('input[id="video"]').files[0];
        const trailerFile = document.querySelector('input[id="trailer"]').files[0];
        const imageFile = document.querySelector('input[id="image"]').files[0];

        const renamedVideo = videoFile ? renameFilePath(videoFile.name, 'video') : null;
        const renamedTrailer = trailerFile ? renameFilePath(trailerFile.name, 'trailer') : null;
        const renamedImage = imageFile ? renameFilePath(imageFile.name, 'image') : null;

        const formData = new FormData();

        formData.append("name", document.querySelector('input[id="movieName"]').value);
        formData.append("movie_time", document.querySelector('input[id="movieTime"]').value);
        formData.append("Publication_year", parseInt(document.querySelector('input[id="publicationYear"]').value));
        // formData.append("Publication_year", parseInt(2022));
        formData.append("description", document.querySelector('textarea[id="description"]').value);
        formData.append("age", parseInt(document.querySelector('input[id="age"]').value));

        if (renamedImage) {
            formData.append("image", imageFile);
        }
        if (renamedVideo) {
            formData.append("movie", videoFile);
        }
        if (renamedTrailer) {
            formData.append("trailer", trailerFile);
        }

        // const selectedCategories = Array.from(document.querySelector('select[id="categories"]').selectedOptions).map(
        //     (option) => option.value
        // );
        // selectedCategories.forEach((category) => formData.append("categories[]", category));

        selectedCategories.forEach((category) => formData.append("categories[]", category));

        updateMovie(movieId, formData).then(isSuccess => {
            if (isSuccess) {
                handleReturn();
            } else {
                console.error('Failed to update movie');
            }
        });
    };

    

    const handleCategoryChange = (e, categoryId) => {
        const updatedCategories = e.target.checked
            ? [...selectedCategories, categoryId] // אם נבחר, נוסיף את הקטגוריה
            : selectedCategories.filter((id) => id !== categoryId); // אם בוטלה הבחירה, נסיר את הקטגוריה
    
        setSelectedCategories(updatedCategories);
    };
    
    


    return (
        <div className="Edit-Movie-body">
            <UpperMenu />
            <div className="Edit-Movie-modal-content">
                <button className="close-btn-new-movie" onClick={handleReturn}>
                    <i className="bi bi-x-lg"></i>
                </button>
                <h2>Edit Movie - "{currentMovie ? currentMovie.name : "Loading..."}"</h2>
                <div className="modal-movie-center">
                    <div className="current-movie">
                        <h3>Current Movie</h3>
                        <div className="input-group">
                            <label>Name:</label>
                            <span>{currentMovie.name}</span>
                        </div>
                        <div className="input-group">
                            <label>Publication Year:</label>
                            <span>{currentMovie.Publication_year}</span>
                        </div>
                        <div className="input-group">
                            <label>Duration:</label>
                            <span>{currentMovie.movie_time}</span>
                        </div>
                        <div className="input-group">
                            <label>Age Restriction:</label>
                            <span>{currentMovie.age ? `${currentMovie.age}+` : "No age restriction"}</span>
                        </div>
                        <div className="input-group">
                            <label>Description:</label>
                            <span>{currentMovie.description || "No description available"}</span>
                        </div>
                        <div className="input-group">
                            <label>Categories:</label>
                            <span>
                                {movieCategories && movieCategories.length > 0
                                    ? movieCategories.map(category => category.name).join(", ")
                                    : "No categories"
                                } </span>
                        </div>
                        <div className="input-group">
                            <label>Video:</label>
                            <span>{currentMovie.video ? <a href={currentMovie.video} target="_blank" rel="noopener noreferrer">Watch Video</a> : "No video available"}</span>
                        </div>
                        <div className="input-group">
                            <label>Trailer:</label>
                            <span>{currentMovie.trailer ? <a href={currentMovie.trailer} target="_blank" rel="noopener noreferrer">Watch Trailer</a> : "No trailer available"}</span>
                        </div>
                        <div className="input-group">
                            <label>Image:</label>
                            <span>{currentMovie.image ? <a href={currentMovie.image} target="_blank" rel="noopener noreferrer">Watch Image</a> : "No image available"}</span>
                        </div>
                    </div>

                    <div className="modal-movie-form">
                        <h3>New Movie</h3>
                        <div className="modal-movie-left">
                            <div className="input-group">
                                <input type="text" id="movieName" placeholder="Movie Name" />
                                <p className="error-message-movie" id="movieName-error">Movie name must be at least 2 characters long.</p>
                            </div>
                            <div className="input-group">
                                <input type="number" id="publicationYear" placeholder="Publication Year" />
                                <p className="error-message-movie" id="publicationYear-error">Year must be between 0 and the current year.</p>
                            </div>
                            <div className="input-group">
                                <input type="text" id="movieTime" placeholder="Duration (e.g., 1:30)" />
                                <p className="error-message-movie" id="movieTime-error">Duration must be in the format "1:30".</p>
                            </div>
                            <div className="input-group">
                                <input type="number" id="age" placeholder="Age Rating" />
                                <p className="error-message-movie" id="age-error">Age must be even and greater than 0.</p>
                            </div>
                            <div className="input-group">
                                <textarea placeholder="Description" id="description"></textarea>
                            </div>
                            <div className="input-group">
                                <h6>Categories</h6>
                                <div className="category-list">
                                    {allCategories.map((category) => (
                                        <div key={category.id} className="category-item">
                                            <input
                                                type="checkbox"
                                                id={`category-${category.id}`}
                                                checked={selectedCategories.includes(category.id)} 
                                                onChange={(e) => handleCategoryChange(e, category.id)}
                                            />
                                            <label htmlFor={`category-${category.id}`} className="category-label">
                                                {category.name}
                                            </label>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                        <div className="modal-movie-right">
                            <div className="input-group">
                                <h6>Movie</h6>
                                <input type="file" id="video" accept="video/*" />
                                <p className="error-message-movie" id="video-error">Please upload a movie file.</p>
                            </div>
                            <div className="input-group">
                                <h6>Movie Picture</h6>
                                <input type="file" id="image" accept="image/*" />
                                <p className="error-message-movie" id="image-error">Please upload a movie picture.</p>
                            </div>
                            <div className="input-group">
                                <h6>Trailer</h6>
                                <input type="file" id="trailer" accept="video/*" />
                                <p className="error-message-movie" id="trailer-error">Please upload a trailer file.</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="Create-Movie-btn">
                    <button onClick={handleSubmit}>Update Movie</button>
                </div>
                <p className="text-decoration-underline" onClick={handleReturn}>Return</p>
            </div >
        </div>
    );
}

export default EditMovie;