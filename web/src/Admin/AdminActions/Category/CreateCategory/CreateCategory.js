import './CreateCategory.css';
import React, { useState, useEffect } from 'react';
import { createCategory } from '../CategoryActions.js';
// import moviesData  from '../../../../data/movies/movies.js';

function CreateCategory() {
    const [categoryName, setCategoryName] = useState('');
    const [isPromotedValue, setIsPromoted] = useState(false);
    // const [selectedMovies, setSelectedMovies] = useState([]);
    // const [movies, setMovies] = useState([]);
    // const [selectedMovies, setSelectedMovies] = useState([]);

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
        const name = categoryName;
        const isPromoted = isPromotedValue;
        const movies = [];
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
                    <label>Promotion</label>
                    <div className="CC-checkbox">
                        <input 
                            type="checkbox" 
                            checked={isPromotedValue} 
                            onChange={(e) => setIsPromoted(e.target.checked)} 
                        />
                    </div>
                </div>
                <div className="input-group">
                    <label>Movies</label>
                    <div className="CC-movie-list">
                        {/* {movies.map((movie, index) => (
                            <div key={index} className="CC-movie-item">
                                <input
                                    type="checkbox"
                                    checked={selectedMovies.includes(index)}
                                    onChange={() => toggleMovieSelection(index)}
                                />
                                <span>{movie}</span>
                            </div>
                        ))} */}
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
