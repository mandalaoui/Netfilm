const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const movieValidation = require('../validation/movie');

// Define routes for '/'
router.route('/')
    .get(movieValidation.validateUserId, movieController.getMovies)
    .post(movieValidation.validateMovieInput, movieController.createMovie);

// Define routes for '/:id'
router.route('/:id')
    .get(movieValidation.validateMovieId,movieController.getMovie)
    .put(movieValidation.validateMovieId, movieValidation.validateMovieInput, movieController.updateMovie)
    .delete(movieValidation.validateMovieId,movieController.deleteMovie);

// Define route for searching movies with a query.
router.route('/search/:query')
    .get(movieController.getMovieIncludeQuery);

// Export the router to be used in the main application.
module.exports = router;