import './MovieRow.css';
import { useRef, useState } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
import movies from '../../data/movies/movies.js'

function MovieRow({ category }) {
    const rowRef = useRef(null);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    const [canScrollRight, setCanScrollRight] = useState(false);

    const handleScroll = () => {
        const container = rowRef.current;
        const containerWidth = container.clientWidth;
        const scrollLeft = container.scrollLeft;
        // const scrollWidth = container.scrollWidth;
        const windowwidth = window.innerWidth;
        // console.log("containerWidth: " + containerWidth);
        // console.log("scrollLeft: " + scrollLeft);
        // console.log("scrollWidth: " + scrollWidth);
        // console.log("windowwidth: " + windowwidth);

        setCanScrollRight(containerWidth > windowwidth);
        setCanScrollLeft(scrollLeft > 0);
    };

    const scrollRow = (direction) => {
        const container = rowRef.current;
        const scrollAmount = 300;
        container.scrollBy({ left: direction === 'left' ? -scrollAmount : scrollAmount, behavior: 'smooth' });
    };

    const filteredMovies = movies.filter((movie) => movie.categories.includes(category));

    return (
        <div
            className="movie-row-container"
            onMouseEnter={handleScroll}
            onMouseLeave={() => {
                setCanScrollLeft(false);
                setCanScrollRight(false);
            }}
        >
            <h6>{category}</h6>
            <div className="movie-row">
                <div className="movie-list" ref={rowRef} onScroll={handleScroll}>
                    {filteredMovies.length === 0 && category === "Watched" ? (
                        <p className="no-movies-text">Haven't seen any movie yet</p>
                    ) : (
                        filteredMovies.map((movie) => (
                            <MovieCard key={movie.id} movie={movie} />
                        ))
                    )}
                </div>
            </div>
            <div className="arrow-container" style={{ width: window.innerWidth }}>
                {canScrollLeft && (
                    <button className="scroll-arrow left" onClick={() => scrollRow('left')}>
                        &#8249;
                    </button>
                )}
                {canScrollRight && (
                    <button className="scroll-arrow right" onClick={() => scrollRow('right')}>
                        &#8250;
                    </button>
                )}
            </div>
        </div>
    );
}

export default MovieRow;
