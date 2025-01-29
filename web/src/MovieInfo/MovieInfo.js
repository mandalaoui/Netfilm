import './MovieInfo.css';
import UpperMenu from '../HomeScreen/UpperMenu/UpperMenu.js';
import TrailerMovieInfo from './TrailerMovieInfo/TrailerMovieInfo.js';
import MovieDetails from './MovieDetails/MovieDetails.js';
import Recommendations from './Recommendations/Recommendations.js'
import Search from '../HomeScreen/Search/Search.js';
import { getMovieById } from '../Admin/AdminActions/Movie/MovieActions.js';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

function MovieInfo() {
    const { id } = useParams();
    const [movie, setMovie] = useState('');
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        // Fetch movie details when movieId is available
        const fetchMovie = async () => {
            if (id) {
                try {
                    const movie = await getMovieById(id);
                    setMovie(movie);
                } catch (error) {
                    console.error("Error fetching movie:", error);
                }
            }
        };

        fetchMovie(); // Call the async function to fetch the movie details
    }, [id]);


    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    return (
        <div className="movie-info-container">
            <UpperMenu searchQuery={searchQuery} onSearchChange={handleSearchChange} />
            {searchQuery === '' ? (
                <>
                <div className='upper-movie-info'>
                    <TrailerMovieInfo movie={movie} />
                    <div className="movie-title-overlay">{movie?.name}</div>
                </div>
                <MovieDetails movie={movie} />
                <Recommendations movie={movie} />
                </>
                ) : (
                    <Search query={searchQuery} />
            )}
            <div className="bottom-space"></div>
        </div>
    );
}

export default MovieInfo;
