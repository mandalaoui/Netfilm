import './MovieRow.css';
import { useRef, useState } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
import movies from '../../data/movies/movies.js'
import { useLocation } from 'react-router-dom';
import { showConfirmationModal } from '../../Admin/Verification/Verification.js';
import { deleteCategory } from '../../Admin/AdminActions/AdminActions.js';
import EditCategory from '../../Admin/AdminActions/Category/EditCategory/EditCategory.js'

function MovieRow({ category }) {
    const rowRef = useRef(null);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    const [canScrollRight, setCanScrollRight] = useState(false);
    const location = useLocation();
    const isAdminPage = location.pathname === "/admin";
    const [showModal, setShowModal] = useState(false);
    

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

    const filteredMovies = movies.filter((movie) => movie.categories.includes(category.name));
    if (!isAdminPage && filteredMovies.length === 0) {
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
        const userId = "6793f41b8221f4dda02b7e63";
        const userConfirmed = await showConfirmationModal("category", category.name, 'delete');
        if (userConfirmed) {
            deleteCategory(category.id, userId)
            .then(isSuccess => {
                if (isSuccess) {
                    // alert(`Category: ${category.name} - deleted`);
                    window.location.href = "/admin";
                }
            });
        } else {
            console.log('Delete action was canceled.');
        }
    };

    const handleAddMovie = () => {
        // Placeholder for add movie functionality
        alert(`Add new movie to category: ${category.name}`);
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
                    {filteredMovies.length === 0 && category.name === "Watched" ? (
                        <p className="no-movies-text">Haven't seen any movie yet</p>
                    ) : (
                        filteredMovies.map((movie) => (
                            <MovieCard key={movie.id} movie={movie} />
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
