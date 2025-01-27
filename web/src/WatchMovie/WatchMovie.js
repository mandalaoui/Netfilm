import './WatchMovie.css';
import View from './View/View.js';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
// import movies from '../data/movies/movies.js'
import { getMovieById } from '../Admin/AdminActions/Movie/MovieActions.js';

function WatchMovie() {
    const { id } = useParams();
    const [movie, setMovie] = useState(null);

    useEffect(() => {
        // Fetch movie data by ID
        getMovieById(id, "67964782c8b5942c5f45547f")
            .then((movieData) => {
                setMovie(movieData); // Update the movie state when the data is fetched
                // setLoading(false); // Set loading to false after fetching
            })
            .catch((error) => {
                console.error("Error fetching movie:", error);
                // setLoading(false); // Stop loading even if there is an error
            });
    }, [id]);

    if (!movie) {
        return <div>Error: Movie not found!</div>;
    }

//     const movie = getMovieById(id, "67964782c8b5942c5f45547f");
//     console.log(movie);

    return (
            <div className="watcher">
                    <View movie={movie} />
            </div>
    );
}

export default WatchMovie;
