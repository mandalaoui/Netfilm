import './WatchMovie.css';
import View from './View/View.js';
import { useParams } from 'react-router-dom';
import movies from '../data/movies/movies.js'

function WatchMovie() {
    const { id } = useParams();
    const movie = movies.find(movie => movie.id === parseInt(id));

    return (
            <div className="watcher">
                    <View movie={movie} />
            </div>
    );
}

export default WatchMovie;
