import './Movies.css';
import React from 'react';
import MovieRow from '../MovieRow/MovieRow.js';

function Movies() {
    return (
        <div className="movies-container">
            <MovieRow category="Watched" />
            <MovieRow category="Action" />
            <MovieRow category="Comedy" />
            <MovieRow category="Drama" />
            <MovieRow category="Family" />
        </div>
    );
}

export default Movies;