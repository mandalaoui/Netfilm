import './Search.css';
import { useEffect, useState } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
// import { getAllMovies } from '../../Admin/AdminActions/Movie/MovieActions.js';

function Search({ query }) {
    // const [moviesByCategory, setMoviesByCategory] = useState([]);
    const [filteredMovies, setFilteredMovies] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);


    // useEffect(() => {
    //     const fetchMovies = async () => {
    //         try {
    //             const allMovies = await getAllMovies();
    //             if (allMovies) {
    //                 const moviesGroupedByCategory = allMovies.map((categoryMovies) => ({
    //                     categoryName: categoryMovies.categoryName,
    //                     movies: categoryMovies.movies
    //                 }));
    //                 setMoviesByCategory(moviesGroupedByCategory);
    //             }
    //         } catch (error) {
    //             console.error("Error fetching movies:", error);
    //         }
    //     };
    //     fetchMovies();
    // }, []);

    // useEffect(() => {
    //     if (query.trim() === "") {
    //         setFilteredMovies([]);
    //         return;
    //     }

    //     const allMovies = moviesByCategory.flatMap((category) => category.movies);

    //     const filtered = allMovies.filter((movie) =>
    //         movie.name.toLowerCase().includes(query.toLowerCase())
    //     );

    //     setFilteredMovies(filtered);
    // }, [query, moviesByCategory]);

    const fetchMoviesByQuery = async (searchQuery) => {
        setIsLoading(true);
        setError(null);

        try {
            const response = await fetch(`http://localhost:12345/api/movies/search/${encodeURIComponent(searchQuery)}`, {
                method: "GET",
                headers: {
                    userId: localStorage.getItem('userId'),
                }
            });

            if (response.ok) {
                const movies = await response.json(); 
                setFilteredMovies(movies); 
            } else {
                throw new Error(`Error: ${response.status} - ${response.statusText}`);
            }
        } catch (err) {
            console.error("Error fetching movies by query:", err);
            setError("Failed to fetch movies. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (query.trim() === "") {
            setFilteredMovies([]);
            return;
        }

        fetchMoviesByQuery(query);
    }, [query]);

    return (
        <div className="search-results">
            {isLoading && <p>Loading...</p>}
            {error && <p className="error">{error}</p>}
            {filteredMovies.length > 0 ? (
                filteredMovies.map((movie) => (
                    <MovieCard key={movie.id} movie={movie} />
                ))
            ) : (
                !isLoading && !error && <p>No results found</p>
            )}
        </div>
    );
}

export default Search;
