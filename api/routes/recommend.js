const express = require('express');
var router = express.Router();
const recommendController = require('../controllers/recommend');
const userValidation = require('../validation/user');

// Define routes for '/:id/recommend'
router.route('/:id/recommend')
    .get(userValidation.validateUserId, recommendController.getRecommendedMovies)
    .post(userValidation.validateUserId, recommendController.addToWatchList)

module.exports = router;