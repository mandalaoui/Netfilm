const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');

router.route('/')
    .get(movieController.getMovies)
    .post(movieController.createMovie);

router.route('/:id')
    .get(movieController.getMovie)
    .put(movieController.updateMovie)
    .delete(movieController.deleteMovie);

module.exports = router;