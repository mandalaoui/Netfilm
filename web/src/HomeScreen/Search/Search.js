import './Search.css';
import { useEffect, useState } from 'react';
import MovieCard from '../MovieCard/MovieCard.js'; 
import { getAllMovies } from '../../Admin/AdminActions/Movie/MovieActions.js';

function Search({ query }) {
    const [moviesByCategory, setMoviesByCategory] = useState([]);
    const [filteredMovies, setFilteredMovies] = useState([]); 
    const userId = "67964782c8b5942c5f45547f";

        useEffect(() => {
            const fetchMovies = async () => {
                try {
                    const allMovies = await getAllMovies(userId); 
                    if (allMovies) {
                        const moviesGroupedByCategory = allMovies.map((categoryMovies) => ({
                            categoryName: categoryMovies.categoryName,
                            movies: categoryMovies.movies
                        }));
                        setMoviesByCategory(moviesGroupedByCategory);
                    }
                } catch (error) {
                    console.error("Error fetching movies:", error);
                }
            };
            fetchMovies();
        }, []);
    
        useEffect(() => {
            if (query.trim() === "") {
                setFilteredMovies([]); 
                return;
            }
    
            const allMovies = moviesByCategory.flatMap((category) => category.movies);
    
            const filtered = allMovies.filter((movie) =>
                movie.name.toLowerCase().includes(query.toLowerCase())
            );
    
            setFilteredMovies(filtered);
        }, [query, moviesByCategory]);

    return (
        <div className="search-results">
            {filteredMovies.length > 0 ? (
                filteredMovies.map((movie) => (
                    <MovieCard key={movie.id} movie={movie} />
                ))
            ) : (
                <p>No results found</p>
            )}
        </div>
    );
}

export default Search;
