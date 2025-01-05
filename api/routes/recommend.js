const express = require('express');
var router = express.Router();
const recommendController = require('../controllers/recommend');

router.route('/:id/recommend')
    .get(recommendController.getRecommendedMovies)
    .post(recommendController.addToWatchList)

module.exports = router;