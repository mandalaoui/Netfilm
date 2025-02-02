import './MovieRow.css';
import React, { useRef, useState, useEffect } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
import { showConfirmationModal } from '../../Admin/Verification/Verification.js';
import { getCategoryById, getAllCategories, createCategory, deleteCategory, updateCategory } from '../../Admin/AdminActions/Category/CategoryActions.js';
import EditCategory from '../../Admin/AdminActions/Category/EditCategory/EditCategory.js'
import { getMovieById } from '../../Admin/AdminActions/Movie/MovieActions.js';
import { useNavigate, useLocation } from 'react-router-dom';

function MovieRow({ category }) {
    const rowRef = useRef(null);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    const [canScrollRight, setCanScrollRight] = useState(false);
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";
    const [showModal, setShowModal] = useState(false);
    const [moviesByCategory, setMoviesByCategory] = useState([]);
    const [isUnAttached, setIsUnAttached] = useState(false);
    const navigate = useNavigate();

    // Effect to check if the category is "unAttached" and set the state accordingly
    useEffect(() => {
        if (category.name === "unAttached")
            setIsUnAttached(true);
    }, [category.name]);

    // Effect to fetch movies related to the category when the component mounts or category changes
    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const movies = await Promise.all(
                    category.movies.map((movieId) => {
                        return getMovieById(movieId);
                    })
                );
                if (Array.isArray(movies) && movies.length > 0) {
                    setMoviesByCategory(movies);
                } else {
                    setMoviesByCategory([]); // במידה ואין סרטים, מנקה את המצב
                }
            } catch (error) {
                // console.error("Error fetching movies by category:", error);
            }
        };
        if (category.movies && category.movies.length > 0) {
            fetchMovies();
        } else {
            setMoviesByCategory([]); // במידה ואין סרטים, מנקה את המצב
        }
    }, [category.movies]);

    // Handler for scrolling functionality to enable/disable left and right scrolling buttons
    const handleScroll = () => {
        const container = rowRef.current;
        const containerWidth = container.clientWidth;
        const scrollLeft = container.scrollLeft;
        const scrollWidth = container.scrollWidth;
        setCanScrollRight(scrollWidth > containerWidth);
        setCanScrollLeft(scrollLeft > 0);
    };

    // Function to scroll the movie row by a certain amount (based on direction)
    const scrollRow = (direction) => {
        const container = rowRef.current;
        const scrollAmount = 0.8 * rowRef.current.clientWidth;
        container.scrollBy({ left: direction === 'left' ? -scrollAmount : scrollAmount, behavior: 'smooth' });
    };

    // If it's not an admin page and there are no movies in the category (excluding "Watched"), don't render this row
    if (!isAdminPage && moviesByCategory.length === 0 && category.name !== "Watched") {
        return null;
    }
    // If it's an admin page and the category is "Watched", don't render this row (as it's handled elsewhere)
    if (isAdminPage && category.name === "Watched") {
        return null;
    }

    // Handler for editing category (show confirmation modal)
    const handleEditCategory = async () => {
        const userConfirmed = await showConfirmationModal("category", category.name, 'edit');
        if (userConfirmed) {
            setShowModal(true);
        }
    };

    // Handler for deleting category
    const handleDeleteCategory = async () => {
        const userConfirmed = await showConfirmationModal("category", category.name, 'delete');
        if (!userConfirmed) {
            return;
        }

        try {
            // Process for deleting the category and handling movies in that category
            const moviesInCategory = category.movies;
            const moviesWithSingleCategory = [];
            // Check each movie to see if it only belongs to this category
            for (const movieId of moviesInCategory) {
                const movie = await getMovieById(movieId);
                if (movie && movie.categories.length === 1) {
                    moviesWithSingleCategory.push(movieId);
                }
            }
            // Fetch all categories to check for the "unAttached" category
            const allCategoryIds = await getAllCategories();
            const allCategories = await Promise.all(
                allCategoryIds.map(async (categoryId) => {
                    const categoryDetails = await getCategoryById(categoryId);
                    return categoryDetails;
                })
            );
            let unAttachedCategory = allCategories.find(cat => cat.name === "unAttached");
            // If "unAttached" category doesn't exist, create it
            if (!unAttachedCategory) {
                const isCreated = await createCategory("unAttached", false, []);
                if (!isCreated) {
                    alert("Failed to create 'unAttached' category. Aborting delete.");
                    return;
                }
                // Re-fetch categories after creating "unAttached"
                const updatedCategoryIds = await getAllCategories();
                const updatedCategories = await Promise.all(
                    updatedCategoryIds.map(async (categoryId) => {
                        const categoryDetails = await getCategoryById(categoryId);
                        return categoryDetails;
                    })
                );

                unAttachedCategory = updatedCategories.find(cat => cat.name === "unAttached");
            }
            // If "unAttached" category exists, update it with the movies that only belong to this category
            if (unAttachedCategory && unAttachedCategory.movies !== undefined) {
                const updatedMovies = [...unAttachedCategory.movies, ...moviesWithSingleCategory];
                const isUpdated = await updateCategory(unAttachedCategory.id, { ...unAttachedCategory, movies: updatedMovies });
                if (!isUpdated) {
                    alert("Failed to update 'unAttached' category. Aborting delete.");
                    return;
                }
                const isDeleted = await deleteCategory(category.id);
                if (isDeleted) {
                    if (location.pathname === '/admin') {
                        navigate('/admin', { replace: true });
                    } else {
                        navigate('/admin');
                    }
                } else {
                    alert(`Failed to delete category: ${category.name}`);
                }
            }
        } catch (error) {
            // console.error("Error during category deletion process:", error);
        }
    };

    // Handler for adding a movie to the current category
    const handleAddMovie = () => {
        navigate(`/admin/CreateMovie?specificCategory=${category.id}`);
    };


    return (
        <div
            className="movie-row-container"
            onMouseEnter={handleScroll}
            onMouseLeave={() => {
                setCanScrollLeft(false);
                setCanScrollRight(false);
            }}
        >
            <div className="category-header">
                <h6 className="category-title">{category.name}</h6>
                {isAdminPage && (
                    <>
                        {!isUnAttached && (
                            <>
                                <h6 className="category-promotion">
                                    {category.isPromoted ? "promoted" : "not promoted"}
                                </h6>
                                <div className="category-actions">
                                    <button className="edit-category-button" onClick={handleEditCategory}>
                                        <i className="bi bi-pencil-square"></i>
                                    </button>
                                    {showModal && 
                                        <div className="modal-edit-category">
                                            <button className="modal-edit-close-button" onClick={() => setShowModal(false)}>X</button>
                                            <EditCategory category={category} />
                                        </div>
                                    }
                                    <button className="delete-category-button" onClick={handleDeleteCategory}>
                                        <i className="bi bi-trash"></i>
                                    </button>
                                </div>
                            </>
                        )}
                    </>
                )}
            </div>
            {canScrollLeft && (
                <div className='left-arrow-wrapper'>
                    <button className="scroll-arrow left" onClick={() => scrollRow('left')}>
                        &#8249;
                    </button>
                </div>
            )}
            <div className="movie-row">
                <div className="movie-list" ref={rowRef} onScroll={handleScroll}>
                    {isAdminPage && (
                        <>
                            {!isUnAttached && (
                                <button className="add-movie-button-to-category" onClick={handleAddMovie}>
                                    <i className="bi bi-plus-circle"></i>
                                </button>
                            )}
                        </>
                    )}
                    {!isAdminPage && category.name === "Watched" && moviesByCategory.length === 0 ? (
                        <p className="no-movies-text">Haven't seen any movie yet</p>
                    ) : (
                        moviesByCategory.map((movie) => (
                            <MovieCard key={movie._id} movie={movie} />
                        ))
                    )}
                </div>
            </div>
            {canScrollRight && (
                <div className='right-arrow-wrapper'>
                    <button className="scroll-arrow right" onClick={() => scrollRow('right')}>
                        &#8250;
                    </button>
                </div>
            )}
        </div>
    );
}

export default MovieRow;
