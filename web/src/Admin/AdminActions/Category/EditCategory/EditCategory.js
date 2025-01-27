import './EditCategory.css';
import React, { useState, useEffect } from 'react';
import { updateCategory } from '../CategoryActions.js';
// import moviesData  from '../../../../data/movies/movies.js';
import { getMovieById } from '../../Movie/MovieActions.js';


function EditCategory({category}) {
    const [categoryName, setCategoryName] = useState(category.name || '');
    const [isPromotedValue, setIsPromoted] = useState(category.isPromoted || false);
    const [movieNames, setMovieNames] = useState([]);

    useEffect(() => {
        // Fetch movie names using the IDs from the category
        const fetchMovieNames = async () => {
            try {
                const names = await Promise.all(
                    category.movies.map(async (movieId) => {
                        const movie = await getMovieById(movieId, "67964782c8b5942c5f45547f");
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
    
    // const [movies, setMovies] = useState(category.movies || []);

    // useEffect(() => {
    //     fetch('http://localhost:12345/api/movies')  
    //         .then(response => response.json())
    //         .then(data => setMovies(data))
    //         .catch(error => {
    //             console.error("Error fetching movies:", error);
    //             setMovies(moviesData);  // אם קרה שגיאה, טוענים את המידע המקומי
    //         });
    // }, []);

    // פונקציה לשינוי סטייט כאשר סרט נבחר או מבוטל
    // const toggleMovieSelection = (movieId) => {
    //     setSelectedMovies((prevSelected) =>
    //         prevSelected.includes(movieId)
    //             ? prevSelected.filter(id => id !== movieId) // אם הסרט כבר נבחר, מסירים אותו
    //             : [...prevSelected, movieId] // אם לא נבחר, מוסיפים אותו לרשימה
    //     );
    // };

    const handleSubmit = () => {
        const userId = '67964782c8b5942c5f45547f';
        const categoryData = {
        name: categoryName,
        isPromoted: isPromotedValue,
        movies: [],
        }
            // movies: selectedMovies,  // סרטים שנבחרו
            updateCategory(category.id, userId, categoryData)
            .then(isSuECess => {
                if (isSuECess) {
                    // document.querySelector('.Create-Category-modal-container').style.display = 'none'; 
                    window.location.href = '/admin';
                }
            });
    };

    return (
        <div className="Edit-Category-modal-container">
            <div className="Edit-Category-modal-content">
                <button className="EC-close-btn" onClick={() => window.location.href = '/admin'}> 
                    <i className="bi bi-x-lg"></i>
                </button>
                <h2>Edit Category</h2>
                <div className="category-layout">
                    <div className="current-category">
                        <h3>Current Category</h3>
                        <div className="input-group">
                            <label>Name:</label>
                            <span>{category.name}</span>
                        </div>
                        <div className="input-group">
                            <label>Promotion:</label>
                            <span>{category.isPromoted ? "Promoted" : "Not Promoted"}</span>
                        </div>
                        <div className="input-group">
                            <label>Movies:</label>
                            <span>
                                {movieNames.length > 0 
                                    ? movieNames.join(", ") 
                                    : "No movies selected"}
                            </span>                        
                        </div>
                    </div>

                    <div className="new-category">
                        <h3>New Category</h3>
                        <div className="input-group">
                            <label>Name:</label>
                            <input 
                                type="text" 
                                placeholder="Category Name" 
                                value={categoryName} 
                                onChange={(e) => setCategoryName(e.target.value)} 
                            />
                        </div>
                        <div className="input-group">
                            <label>Promotion:</label>
                            <div className="EC-checkbox">
                                <input 
                                    type="checkbox" 
                                    checked={isPromotedValue} 
                                    onChange={(e) => setIsPromoted(e.target.checked)} 
                                />
                            </div>
                        </div>
                        <div className="input-group">
                            <label>Movies</label>
                            <div className="EC-movie-list">
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
