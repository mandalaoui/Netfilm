const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const movieValidation = require('../validation/movie');
const userValidation = require('../validation/user');
const recommendcontroller = require('../controllers/recommend');


// Define routes for '/'
router.route('/')
    .get(userValidation.validateUserIdHeader, movieController.getMovies)
    .post( userValidation.validateUserIdHeader, movieValidation.validateMovieInput, movieController.createMovie);

// Define routes for '/:id'
router.route('/:id')
    .get(userValidation.validateUserIdHeader, movieValidation.validateMovieId, movieController.getMovie)
    .put(userValidation.validateUserIdHeader, movieValidation.validateMovieId, movieValidation.validateMovieInput, recommendcontroller.deleteMovie, movieController.updateMovie)
    .delete(userValidation.validateUserIdHeader, movieValidation.validateMovieId, recommendcontroller.deleteMovie, movieController.deleteMovie);

// Define route for searching movies with a query.
router.route('/search/:query')
    .get(userValidation.validateUserIdHeader, movieController.getMovieIncludeQuery);

// Export the router to be used in the main application.
module.exports = router;