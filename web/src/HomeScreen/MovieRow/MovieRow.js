import './MovieRow.css';
import React, { useRef, useState, useEffect } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
// import movies from '../../data/movies/movies.js'
// import { getAllMovies } from '../../Admin/AdminActions/Movie/MovieActions.js';
import { useLocation } from 'react-router-dom';
import { showConfirmationModal } from '../../Admin/Verification/Verification.js';
import { getCategoryById, getAllCategories, createCategory, deleteCategory, updateCategory } from '../../Admin/AdminActions/Category/CategoryActions.js';
import EditCategory from '../../Admin/AdminActions/Category/EditCategory/EditCategory.js'
// import CreateMovie from '../../Admin/AdminActions/Movie/CreateMovie/CreateMovie.js';
import { getMovieById } from '../../Admin/AdminActions/Movie/MovieActions.js';


function MovieRow({ category }) {
    const rowRef = useRef(null);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    const [canScrollRight, setCanScrollRight] = useState(false);
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";
    const [showModal, setShowModal] = useState(false);
    // const [showMovieModal, setShowMovieModal] = useState(false);

    const [moviesByCategory, setMoviesByCategory] = useState([]);
    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const movies = await Promise.all(
                    category.movies.map((movieId) => {
                        return getMovieById(movieId);
                    })
                );
                setMoviesByCategory(movies);
            } catch (error) {
                console.error("Error fetching movies by category:", error);
            }
        };
        if (category.movies && category.movies.length > 0) {
            fetchMovies();
        } else {
            // console.log("No movies found for this category.");
        }
    }, [category.movies]);

    const handleScroll = () => {
        const container = rowRef.current;
        const containerWidth = container.clientWidth;
        const scrollLeft = container.scrollLeft;
        const scrollWidth = container.scrollWidth;
        setCanScrollRight(scrollWidth > containerWidth);
        setCanScrollLeft(scrollLeft > 0);
    };

    const scrollRow = (direction) => {
        const container = rowRef.current;
        const scrollAmount = 0.8 * rowRef.current.clientWidth;
        container.scrollBy({ left: direction === 'left' ? -scrollAmount : scrollAmount, behavior: 'smooth' });
    };

    if (!isAdminPage && moviesByCategory.length === 0) {
        return null;
    }

    const handleEditCategory = async () => {
        // const userId = "6793f41b8221f4dda02b7e63";
        const userConfirmed = await showConfirmationModal("category", category.name, 'edit');
        if (userConfirmed) {
            setShowModal(true);
        }
        //     const CategotyData = null;
        //     updateCategory(category.id, userId, CategotyData)
        //     .then(isSuccess => {
        //         if (isSuccess) {
        //             // alert(`Category: ${category.name} - deleted`);
        //             window.location.href = "/admin";
        //         }
        //     });
        // } else {
        //     console.log('Delete action was canceled.');
        // }

    };

    const handleDeleteCategory = async () => {
        const userConfirmed = await showConfirmationModal("category", category.name, 'delete');
        if (!userConfirmed) {
            console.log('Delete action was canceled.');
            return;
        }

        try {
            // שליפת כל הסרטים בקטגוריה
            const moviesInCategory = category.movies; // assuming `category.movies` is an array of movie IDs
            console.log('Movies in category:', moviesInCategory);

            // בדיקה לסרטים עם קטגוריה יחידה
            const moviesWithSingleCategory = [];
            for (const movieId of moviesInCategory) {
                const movie = await getMovieById(movieId);
                if (movie && movie.categories.length === 1) { // יש רק קטגוריה אחת לסרט
                    moviesWithSingleCategory.push(movieId);
                }
            }
            console.log('Movies with single category:', moviesWithSingleCategory);

            const allCategoryIds = await getAllCategories();
            const allCategories = await Promise.all(
                allCategoryIds.map(async (categoryId) => {
                    const categoryDetails = await getCategoryById(categoryId);
                    return categoryDetails;
                })
            );
            // טיפול בקטגוריה "unAttached"
            let unAttachedCategory = allCategories.find(cat => cat.name === "unAttached");
            if (!unAttachedCategory) {
                console.log('Creating "unAttached" category...');
                const isCreated = await createCategory("unAttached", false, []);
                if (!isCreated) {
                    alert("Failed to create 'unAttached' category. Aborting delete.");
                    return;
                }
                console.log('"unAttached" categoryCreated !');

                // שליפת הקטגוריה החדשה מהשרת (כדי לקבל את ה-ID שלה)
                const updatedCategoryIds = await getAllCategories();
            const updatedCategories = await Promise.all(
                updatedCategoryIds.map(async (categoryId) => {
                    const categoryDetails = await getCategoryById(categoryId);
                    return categoryDetails;
                })
            );

            // חפש את הקטגוריה "unAttached" במערך המעודכן
            unAttachedCategory = updatedCategories.find(cat => cat.name === "unAttached");
            }
            console.log(`unAttachedCategory done!${unAttachedCategory}`);

            if (unAttachedCategory && unAttachedCategory.movies !== undefined) {
                console.log('it is not undefined!');
                const updatedMovies = [...unAttachedCategory.movies, ...moviesWithSingleCategory];
                const isUpdated = await updateCategory(unAttachedCategory.id, { ...unAttachedCategory, movies: updatedMovies });
                if (!isUpdated) {
                    alert("Failed to update 'unAttached' category. Aborting delete.");
                    return;
                }

                console.log(`Movies added to 'unAttached': ${moviesWithSingleCategory}`);
                const isDeleted = await deleteCategory(category.id);
                if (isDeleted) {
                    window.location.href = "/admin";
                } else {
                    alert(`Failed to delete category: ${category.name}`);
                }
            }
        } catch (error) {
            console.error("Error during category deletion process:", error);
        }
    };

    const handleAddMovie = () => {
        window.location.href = `/admin/CreateMovie?specificCategory=${category.id}`;
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
                        <h6 className="category-promotion">
                            {category.isPromoted ? "promoted" : "not promoted"}
                        </h6>
                        <div className="category-actions">
                            <button className="edit-category-button" onClick={handleEditCategory}>
                                <i className="bi bi-pencil-square"></i>
                            </button>
                            {showModal && <EditCategory category={category} />}
                            <button className="delete-category-button" onClick={handleDeleteCategory}>
                                <i className="bi bi-trash"></i>
                            </button>
                        </div>
                    </>
                )}
            </div>
            <div className="movie-row">
                <div className="movie-list" ref={rowRef} onScroll={handleScroll}>
                    {isAdminPage && (
                        <button className="add-movie-button" onClick={handleAddMovie}>
                            <i className="bi bi-plus-circle"></i>
                        </button>
                    )}
                    {moviesByCategory.length === 0 && category.name === "Watched" ? (
                        <p className="no-movies-text">Haven't seen any movie yet</p>
                    ) : (
                        moviesByCategory.map((movie) => (
                            <MovieCard key={movie._id} movie={movie} />
                        ))
                    )}
                </div>
            </div>
            <div className="arrow-container" style={{ width: window.innerWidth }}>
                {canScrollLeft && (
                    <button className="scroll-arrow left" onClick={() => scrollRow('left')}>
                        &#8249;
                    </button>
                )}
                {canScrollRight && (
                    <button className="scroll-arrow right" onClick={() => scrollRow('right')}>
                        &#8250;
                    </button>
                )}
            </div>
        </div>
    );
}

export default MovieRow;
