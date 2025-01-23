import './MovieRow.css';
import { useRef, useState } from 'react';
import MovieCard from '../MovieCard/MovieCard.js';
import movie1 from '../../data/pictures/img1.jpg'

function MovieRow({ category }) {
    const movies = [
        {
            id: 1,
            title: 'Movie 1',
            image: movie1,
            duration: '1h 30m',
            releaseYear: '2022',
            ageRating: 'PG',
            categories: ['Action', 'Adventure', 'Drama']
        },
        {
            id: 2,
            title: 'Movie 2',
            image: movie1,
            duration: '2h 15m',
            releaseYear: '2021',
            ageRating: 'PG-13',
            categories: ['Comedy', 'Drama']
        },
        {
            id: 3,
            title: 'Movie 3',
            image: movie1,
            duration: '2h 0m',
            releaseYear: '2023',
            ageRating: 'R',
            categories: ['Thriller', 'Crime', 'Drama']
        },
        {
            id: 4,
            title: 'Movie 4',
            image: movie1,
            duration: '1h 45m',
            releaseYear: '2020',
            ageRating: 'G',
            categories: ['Drama', 'Family']
        },
        {
            id: 5,
            title: 'Movie 5',
            image: movie1,
            duration: '1h 45m',
            releaseYear: '2020',
            ageRating: 'G',
            categories: ['Drama', 'Family']
        },
        {
            id: 6,
            title: 'Movie 6',
            image: movie1,
            duration: '1h 45m',
            releaseYear: '2020',
            ageRating: 'G',
            categories: ['Drama', 'Family']
        },
        {
            id: 7,
            title: 'Movie 7',
            image: movie1,
            duration: '1h 45m',
            releaseYear: '2020',
            ageRating: 'G',
            categories: ['Drama', 'Family']
        },
    ];

    const rowRef = useRef(null);
    const [canScrollLeft, setCanScrollLeft] = useState(false);
    const [canScrollRight, setCanScrollRight] = useState(false);

    const handleScroll = () => {
        const container = rowRef.current;
        const containerWidth = container.clientWidth;
        const scrollLeft = container.scrollLeft;
        const scrollWidth = container.scrollWidth;
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
                    {filteredMovies.map((movie) => (
                        <MovieCard key={movie.id} movie={movie} />
                    ))}
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
