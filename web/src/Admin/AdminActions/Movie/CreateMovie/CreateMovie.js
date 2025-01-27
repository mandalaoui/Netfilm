import './CreateMovie.css';
import UpperMenu from '../../../../HomeScreen/UpperMenu/UpperMenu.js';
import React, { useState, useEffect } from 'react';
import { createMovie } from '../MovieActions.js';
import { useLocation } from 'react-router-dom';
import { getCategoryById } from '../../Category/CategoryActions.js';

function CreateMovie() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const specificCategory = queryParams.get('specificCategory');
    const [currentCategory, setCurrentCategory] = useState(null);
    
    useEffect(() => {
        console.log(specificCategory);
        if (specificCategory) {
            getCategoryById(specificCategory, "67964782c8b5942c5f45547f")
                .then((category) => {
                    setCurrentCategory(category); // Update state when the category is fetched
                })
                .catch((error) => {
                    console.error("Error fetching category:", error);
                });
        }
    }, [specificCategory]);

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

        console.log(specificCategory);
        let categories = ["6795fa4bc8b5942c5f450886"];
        if (specificCategory != null)
            categories = [specificCategory];
        categories.forEach(category => formData.append("categories[]", category));

        createMovie(formData).then(isSuccess => {
            if (isSuccess) {
                //window.location.href = '/admin';
            }
        });
    };

    const handleReturn = () => {
        window.location.href = '/admin';
    };

    return (
        <div className="Create-Movie-body">
            <UpperMenu />
            <div className="Create-Movie-modal-content">
                <button className="close-btn" onClick={() => window.location.href = '/admin'}>
                    <i className="bi bi-x-lg"></i>
                </button>
                <h2>Create New Movie</h2>
                <div className="modal-movie-center">
                    <div className="modal-movie-left">
                        <div className="input-group">
                            <input type="text" id="movieName" placeholder="Movie Name" />
                            <p className="error-message-movie" id="movieName-error">Movie name must be at least 2 characters long.</p>
                        </div>
                        <div className="input-group">
                            <input type="text" id="movieTime" placeholder="Duration (e.g., 1:30)" />
                            <p className="error-message-movie" id="movieTime-error">Duration must be in the format "1:30".</p>
                        </div>
                        <div className="input-group">
                            <input type="number" id="publicationYear" placeholder="Publication Year" />
                            <p className="error-message-movie" id="publicationYear-error">Year must be between 0 and the current year.</p>
                        </div>
                        <div className="input-group">
                            <input type="number" id="age" placeholder="Age Rating" />
                            <p className="error-message-movie" id="age-error">Age must be even and greater than 0.</p>
                        </div>
                        <div className="input-group">
                            <textarea placeholder="Description" id="description" ></textarea>
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
                        <div className="input-group">
                            {currentCategory ? (
                                <>
                                <h6>Category</h6>
                                <div className="category-display">{currentCategory.name}</div>
                                </>
                                ) : (
                                <textarea placeholder="Categories" id="categories"/>
                            )}
                        </div>
                    </div>
                </div>
                <div className="Create-Movie-btn">
                    <button onClick={handleSubmit}>Create Movie</button>
                </div>
                <p className="text-decoration-underline" onClick={handleReturn}>Return</p>
            </div>
        </div >
    );
}

export default CreateMovie;
