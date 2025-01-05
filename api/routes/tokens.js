const express = require('express');
var router = express.Router();
const tokensController = require('../controllers/tokens');

// Defines the route for user authentication
router.route('/')
    .post(tokensController.autentication);

module.exports = router;