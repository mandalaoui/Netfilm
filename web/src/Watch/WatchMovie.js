import './WatchMovie.css';
import View from './View/View.js';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
// import movies from '../data/movies/movies.js'
import { getMovieById } from '../Admin/AdminActions/Movie/MovieActions.js';
import { addMovieToWatchlist } from '../Functions/UserFunctions.js';

function WatchMovie() {
    const { id } = useParams();
    const [movie, setMovie] = useState(null);

    useEffect(() => {
        // Fetch movie data by ID
        getMovieById(id)
            .then((movieData) => {
                setMovie(movieData); // Update the movie state when the data is fetched
                addMovieToWatchlist(id);
            })
            .catch((error) => {
                console.error("Error fetching movie:", error);
            });
    }, [id]);

    if (!movie) {
        return <div>Error: Movie not found!</div>;
    }

    return (
            <div className="watcher">
                    <View movie={movie.video} />
            </div>
    );
}

export default WatchMovie;
