const movieService = require('../services/movie');

// Function to create a new movie
const createMovie = async (req, res) => {
    const newMovie = await movieService.createMovie(req.body.name, req.body.categories, req.body.movie_time,
        req.body.image, req.body.Publication_year, req.body.description,req.body.videoUrl, req.body.age);
    const location = `/api/movies/${newMovie._id}`;
    res.status(201).location(location).json();
};

// Function to get movies based on categories for a user.
const getMovies = async (req, res) => {
    const userId = req.header('userId');
    const moviesByCategories = await movieService.getMoviesByCategories(userId);

    // If no movies are found, responds with a 400 error
    if(!moviesByCategories) {
        res.status(404).json({ error: ['Movie Not Found'] });
    }
    res.status(200).json(moviesByCategories);
};

// Function to get a movie by its ID.
const getMovie = async (req, res) => {
    const movie = await movieService.getMovieById(req.params.id);

    // If no movie is found, responds with a 404 error
    if (!movie) {
        return res.status(404).json({ errors: ['Movie Not Found'] });
    }
    res.status(200).json(movie);    
};

// Function to update an existing movie's details.
const updateMovie = async (req, res) => {
    const movie = await movieService.updateMovie(req.params.id, req.body.name, req.body.categories, req.body.movie_time,
        req.body.image, req.body.Publication_year, req.body.description,req.body.videoUrl, req.body.age);

    // If no movie is found to update, responds with a 404 error
    if (!movie) {
        return res.status(404).json({ errors: ['Movie Not Found'] });
    }
    res.status(204).json();
};

// Function to delete a movie by its ID
const deleteMovie = async (req, res) => {
    const movie = await movieService.deleteMovie(req.params.id);

    // If no movie is found to delete, responds with a 404 error
    if (!movie) {
        return res.status(404).json({ errors: ['Movie Not Found'] });
    }
    res.status(204).json();
};

// Function to search for movies using a query parameter.
const getMovieIncludeQuery = async (req, res) => {
    const query = req.params.query;

    // If no query is provided, responds with a 400 error
    if (!query) {
        return res.status(400).json({ error: ['Search query is required'] });
    }

    // Calls the movieIncludeQuery function from movieService with the search query.
    const movies = await movieService.movieIncludeQuery(query);

    if (!movies || movies.length === 0) {
        return res.status(404).json({ error: ['No movies found'] });
    }
    res.status(200).json(movies);

};

// Exporting all functions to be used in routes
module.exports = {createMovie, getMovies, getMovie, updateMovie, deleteMovie, getMovieIncludeQuery };