const movieService = require('../services/movie');

const createMovie = async (req, res) => {
    res.json(await movieService.createMovie(req.body.name, req.body.categories, req.body.movie_time,
        req.body.image, req.body.Publication_year, req.body.description, req.body.age));
};

const getMovies = async (req, res) => {
    
    const moviesByCategories = await movieService.getMoviesByCategories(req.query.userId);
    if(!moviesByCategories) {
        res.status(400).json({ error: ['Movie Not Found'] });
    }
    res.status(200).json(moviesByCategories);
};

const getMovie = async (req, res) => {
    const movie = await movieService.getMovieById(req.params.id);
    if (!movie) {
        return res.status(404).json({ errors: ['Movie Not Found'] });
    }
    res.json(movie);    
};

const updateMovie = async (req, res) => {
    const movie = await movieService.updateMovie(req.params.id, req.body.name, req.body.categories, req.body.movie_time,
        req.body.image, req.body.Publication_year, req.body.description, req.body.age);
    if (!movie) {
        return res.status(404).json({ errors: ['Movie Not Found'] });
    }
    res.json(movie);
};

const deleteMovie = async (req, res) => {
    const movie = await movieService.deleteMovie(req.params.id);
    if (!movie) {
        return res.status(404).json({ errors: ['Movie Not Found'] });
    }
    res.json(movie);
};

const getMovieIncludeQuery = async (req, res) => {
    const query = req.params.query;

    if (!query) {
        return res.status(400).json({ error: ['Search query is required'] });
    }
    const movies = await movieService.movieIncludeQuery(query);

    if (!movies || movies.length === 0) {
        return res.status(404).json({ error: ['No movies found'] });
    }
    res.status(200).json(movies);

};



module.exports = {createMovie, getMovies, getMovie, updateMovie, deleteMovie, getMovieIncludeQuery };