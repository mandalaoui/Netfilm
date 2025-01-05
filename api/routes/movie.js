const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const movieValidation = require('../validation/movie');

// Define routes for '/'
router.route('/')
    .get(movieValidation.validateUserId, movieController.getMovies)
    .post(movieValidation.validateMovieInput, movieValidation.validateUserId, movieController.createMovie);

// Define routes for '/:id'
router.route('/:id')
    .get(movieValidation.validateMovieId, movieValidation.validateUserId, movieController.getMovie)
    .put(movieValidation.validateMovieId, movieValidation.validateUserId, movieValidation.validateMovieInput, movieController.updateMovie)
    .delete(movieValidation.validateMovieId, movieValidation.validateUserId, movieController.deleteMovie);

// Define route for searching movies with a query.
router.route('/search/:query')
    .get(movieValidation.validateUserId, movieController.getMovieIncludeQuery);

// Export the router to be used in the main application.
module.exports = router;