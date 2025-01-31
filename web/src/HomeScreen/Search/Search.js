import './Search.css';
import { useEffect, useState } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
import { useGlobalContext } from '../../GlobalContext.js';

function Search({ query }) {
    // useState hook to manage state for filtered movies, loading, and errors
    const [filteredMovies, setFilteredMovies] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const { fileUrl } = useGlobalContext();

    useEffect(() => {
        // Function to fetch movies based on the search query
        const fetchMoviesByQuery = async (searchQuery) => {
            setIsLoading(true);
            setError(null);

            try {
                // Making a GET request to the backend with the search query
                const response = await fetch(`${fileUrl}movies/search/${encodeURIComponent(searchQuery)}`, {
                    method: "GET",
                    headers: {
                        userId: localStorage.getItem('userId'),
                    }
                });

                // If the response is successful (status 200-299)
                if (response.ok) {
                    const movies = await response.json();
                    setFilteredMovies(movies);
                } else {
                    // If the response is not ok, throw an error
                    throw new Error(`Error: ${response.status} - ${response.statusText}`);
                }
            } catch (err) {
                console.error("Error fetching movies by query:", err);
                setError("Failed to fetch movies. Please try again.");
            } finally {
                setIsLoading(false);
            }
        };

        // If the query is empty or consists only of spaces, clear the filtered movies
        if (query.trim() === "") {
            setFilteredMovies([]);
            return;
        }

        // Fetch movies based on the new query
        fetchMoviesByQuery(query);
    }, [query, fileUrl]);

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
