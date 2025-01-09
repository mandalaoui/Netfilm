const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const movieValidation = require('../validation/movie');
const userValidation = require('../validation/user');
const recommendcontroller = require('../controllers/recommend');


// Define routes for '/'
router.route('/')
    .get(userValidation.validateUserId, movieController.getMovies)
    .post(movieValidation.validateMovieInput, userValidation.validateUserId, movieController.createMovie);

// Define routes for '/:id'
router.route('/:id')
    .get(movieValidation.validateMovieId, userValidation.validateUserId, movieController.getMovie)
    .put(movieValidation.validateMovieId, userValidation.validateUserId, movieValidation.validateMovieInput, recommendcontroller.deleteMovie, movieController.updateMovie)
    .delete(movieValidation.validateMovieId, userValidation.validateUserId, recommendcontroller.deleteMovie, movieController.deleteMovie);

// Define route for searching movies with a query.
router.route('/search/:query')
    .get(userValidation.validateUserId, movieController.getMovieIncludeQuery);

// Export the router to be used in the main application.
module.exports = router;