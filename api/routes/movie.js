const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const movieValidation = require('../validation/movie');

router.route('/')
    .get(movieController.getMovies)
    .post(movieValidation.validateMovieInput, movieController.createMovie);

router.route('/:id')
    .get(movieController.getMovie)
    .put(movieController.updateMovie)
    .delete(movieController.deleteMovie);

router.route('/search/:query')
    .get(movieController.getMovieIncludeQuery);

module.exports = router;