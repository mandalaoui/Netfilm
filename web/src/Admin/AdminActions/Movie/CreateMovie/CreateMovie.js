import './CreateMovie.css';
import UpperMenu from '../../../../HomeScreen/UpperMenu/UpperMenu.js';
import React, { useState, useEffect } from 'react';
import { createMovie } from '../MovieActions.js';
import { useLocation } from 'react-router-dom';
import { getCategoryById, getAllCategories } from '../../Category/CategoryActions.js';
import { useNavigate } from 'react-router-dom';

function CreateMovie() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const specificCategory = queryParams.get('specificCategory');
    const [currentCategory, setCurrentCategory] = useState(null);
    const [allCategories, setAllCategories] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [image, setImage] = useState(null);
    const [video, setVideo] = useState(null);
    const [trailer, setTrailer] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch all categories for selection
        const fetchAllCategories = async () => {
            try {
                const categories = await getAllCategories();
                const allCategoryDetails = await Promise.all(
                    categories.map(async (category) => {
                        const fullCategory = await getCategoryById(category);
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
        if (specificCategory) {
            getCategoryById(specificCategory)
                .then((category) => {
                    setCurrentCategory(category); // Update state when the category is fetched
                })
                .catch((error) => {
                    // console.error("Error fetching category:", error);
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

    const handleFileChange = (event, setFile) => {
        const file = event.target.files[0];
        setFile(file);
    };

    const validateFiles = () => {
        const allowedImageTypes = ['image/jpeg', 'image/jpg', 'image/png'];
        const allowedVideoTypes = ['video/mp4', 'video/avi', 'video/mov', 'video/webm'];

        if (!image) {
            alert('Please upload an image.');
            return false;
        }
        if (!allowedImageTypes.includes(image.type)) {
            alert('Invalid image type. Allowed types are JPEG, JPG, PNG.');
            return false;
        }

        if (!video) {
            alert('Please upload a video.');
            return false;
        }
        if (!allowedVideoTypes.includes(video.type)) {
            alert('Invalid video type. Allowed types are MP4, AVI, MOV, WebM.');
            return false;
        }

        if (!trailer) {
            alert('Please upload a trailer.');
            return false;
        }
        if (!allowedVideoTypes.includes(trailer.type)) {
            alert('Invalid trailer type. Allowed types are MP4, AVI, MOV, WebM.');
            return false;
        }

        return true;
    };


    const handleSubmit = () => {
        const renameFilePath = (filePath, newName) => {
            const ext = filePath.split('.').pop();
            return `C:\\fakepath\\${newName}.${ext}`;
        };
        const videoFile = document.querySelector('input[id="video"]').files[0];
        const trailerFile = document.querySelector('input[id="trailer"]').files[0];
        const imageFile = document.querySelector('input[id="image"]').files[0];

        if (!validateFiles()) {
            return;
        }

        const renamedVideo = videoFile ? renameFilePath(videoFile.name, 'video') : null;
        const renamedTrailer = trailerFile ? renameFilePath(trailerFile.name, 'trailer') : null;
        const renamedImage = imageFile ? renameFilePath(imageFile.name, 'image') : null;

        const formData = new FormData();

        formData.append("name", document.querySelector('input[id="movieName"]').value);
        formData.append("movie_time", document.querySelector('input[id="movieTime"]').value);
        formData.append("Publication_year", parseInt(document.querySelector('input[id="publicationYear"]').value));
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

        let categories = [];
        if (specificCategory !== null)
            categories = [specificCategory];
        else if(selectedCategories.length === 0 || selectedCategories == null)
        {
            alert("At least one category must be chosen");
            return;
        }
        else
            categories = selectedCategories;

        categories.forEach(category => formData.append("categories[]", category));

        createMovie(formData).then(isSuccess => {
            if (isSuccess) {
                handleReturn();
            }
        });
    };

    const handleReturn = () => {
        navigate('/admin');
    };

    const handleCategoryChange = (e, categoryId) => {
        const updatedCategories = e.target.checked
            ? [...selectedCategories, categoryId] 
            : selectedCategories.filter((id) => id !== categoryId); 

        setSelectedCategories(updatedCategories);
    };

    return (
        <div className="Create-Movie-body">
            <UpperMenu />
            <div className="Create-Movie-modal-content">
                <button className="close-btn-c-m" onClick={() => navigate('/admin')}>
                    <i className="bi bi-x-lg"></i>
                </button>
                <h2>Create New Movie</h2>
                <div className="modal-movie-center">
                    <div className="modal-movie-left">
                        <div className="input-group-c-m">
                            <input type="text" id="movieName" placeholder="Movie Name" />
                            <p className="error-message-movie" id="movieName-error">Movie name must be at least 2 characters long.</p>
                        </div>
                        <div className="input-group-c-m">
                            <input type="text" id="movieTime" placeholder="Duration (e.g., 1:30)" />
                            <p className="error-message-movie" id="movieTime-error">Duration must be in the format "1:30".</p>
                        </div>
                        <div className="input-group-c-m">
                            <input type="number" id="publicationYear" placeholder="Publication Year" />
                            <p className="error-message-movie" id="publicationYear-error">Year must be between 0 and the current year.</p>
                        </div>
                        <div className="input-group-c-m">
                            <input type="number" id="age" placeholder="Age Rating" />
                            <p className="error-message-movie" id="age-error">Age must be even and greater than 0.</p>
                        </div>
                        <div className="input-group-c-m">
                            <textarea placeholder="Description" id="description" ></textarea>
                        </div>
                    </div>
                    <div className="modal-movie-right">
                        <div className="input-group-c-m">
                            <h6>Movie - Video</h6>
                            <input 
                                type="file" 
                                id="video" 
                                accept="video/mp4,video/avi,video/mov,video/webm"
                                onChange={(e) => handleFileChange(e, setVideo)} 
                            />
                            <p className="error-message-movie" id="video-error">Please upload a movie file.</p>
                        </div>
                        <div className="input-group-c-m">
                            <h6>Image</h6>
                            <input 
                                type="file" 
                                id="image" 
                                accept="image/jpeg,image/jpg,image/png" 
                                onChange={(e) => handleFileChange(e, setImage)}
                            />
                            <p className="error-message-movie" id="image-error">Please upload a movie picture.</p>
                        </div>
                        <div className="input-group-c-m">
                            <h6>Trailer</h6>
                            <input 
                                type="file" 
                                id="trailer" 
                                accept="video/mp4,video/avi,video/mov,video/webm"
                                onChange={(e) => handleFileChange(e, setTrailer)}
                            />
                            <p className="error-message-movie" id="trailer-error">Please upload a trailer file.</p>
                        </div>
                        <div className="input-group-c-m">
                            <h6>Categories</h6>
                            {specificCategory ? (
                                <small>• {currentCategory?.name}</small>
                            ) : (
                                <div className="categories-in-movie-list">
                                    {allCategories.map((category) => (
                                        <div key={category.id} className="categories-in-movie-item">
                                            <input
                                                type="checkbox"
                                                id={`categories-in-movie-${category.id}`}
                                                checked={selectedCategories.includes(category.id)}
                                                onChange={(e) => handleCategoryChange(e, category.id)}
                                            />
                                            <label htmlFor={`categories-in-movie-${category.id}`} className="categories-in-movie-label">
                                                {category.name}
                                            </label>
                                        </div>
                                    ))}
                                </div>
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
