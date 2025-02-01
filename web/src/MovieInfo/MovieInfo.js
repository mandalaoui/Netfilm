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
    const { id } = useParams();  // Get movie ID from URL parameters
    const [currentMovie, setMovie] = useState(null);  // Movie state to store the fetched movie data
    const [searchQuery, setSearchQuery] = useState('');  // State for search query

    useEffect(() => {
        // Fetch movie details when movieId is available
        const fetchMovie = async () => {
            if (id) {
                try {
                    const movie = await getMovieById(id);
                    setMovie(movie);
                } catch (error) {
                    // console.error("Error fetching movie:", error);
                }
            }
        };
        fetchMovie(); 
    }, [id]);


    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    return (
        <div className="movie-info-container">
            <UpperMenu searchQuery={searchQuery} onSearchChange={handleSearchChange} />
            {searchQuery === '' ? (
                currentMovie ? ( // Make sure movie is not null before rendering dependent components
                    <>
                        <div className='upper-movie-info'>
                            <TrailerMovieInfo movie={currentMovie} />
                            <div className="movie-title-overlay">{currentMovie?.name}</div>
                        </div>
                        <MovieDetails movie={currentMovie} />
                        <Recommendations movie={currentMovie} />
                    </>
                ) : (
                    <p>Loading movie details...</p>  // Show loading text until movie data is fetched
                )
                ) : (
                    <Search query={searchQuery} />  // Render search results if there is a search query
                )}
            <div className="bottom-space"></div>
        </div>
    );
}

export default MovieInfo;
