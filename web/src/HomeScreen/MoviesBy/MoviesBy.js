import './MoviesBy.css';
import { useEffect, useState } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
import { useGlobalContext } from '../../GlobalContext.js';
import { getMovieById } from '../../Admin/AdminActions/Movie/MovieActions.js';

function MoviesBy({ category }) {
    const { isLoading, error } = useGlobalContext();
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        const fetchMovies = async () => {
            if (category?.movies && Array.isArray(category.movies)) {
                try {
                    const movieDetails = [];
                    for (const movieId of category.movies) {
                        const movie = await getMovieById(movieId);
                        if (movie) {
                            movieDetails.push(movie); 
                        }
                    }
                    setMovies(movieDetails); 
                } catch (error) {
                    console.error("Error fetching movie details:", error);
                }
            }
        };
        fetchMovies();
    }, [category]);

    return (
        <div className="specific-category">
            {isLoading && <p>Loading...</p>}
            {error && <p className="error">{error}</p>}
            {movies.length > 0 ? (
                movies.map((movie) => (
                    <MovieCard key={movie.id} movie={movie} />
                ))
            ) : (
                <p>No movies found for this category.</p>
            )}
        </div>
    );
}

export default MoviesBy;
