const express = require('express');
var router = express.Router();
const recommendController = require('../controllers/recommend');
const userController = require('../controllers/user');
const userValidation = require('../validation/user');

// Define routes for '/:id/recommend'
router.route('/:id/recommend')
    .get(userValidation.validateUserIdHeader, recommendController.getRecommendedMovies)
    .post(userValidation.validateUserIdHeader, recommendController.addToWatchList)

module.exports = router;