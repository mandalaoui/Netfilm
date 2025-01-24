import './Search.css';
import movies from '../../data/movies/movies.js';
import MovieCard from '../MovieCard/MovieCard.js'; 

function Search({ query }) {
    const filteredMovies = movies.filter(movie =>
        movie.title.toLowerCase().includes(query.toLowerCase())
    );

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
