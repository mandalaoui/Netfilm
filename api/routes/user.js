const express = require('express');
var router = express.Router();
const userController = require('../controllers/user');
const userValidation = require('../validation/user')

// Route for creating a new user. Validates input before calling the controller's createUser function.
router.route('/')
    .post(userValidation.validateUserInput, userController.createUser);

// Route for retrieving a user by their ID. Calls the controller's getUser function.
router.route('/:id')
    .get(userValidation.validateUserId, userValidation.validateUserIdHeader, userController.getUser)

// Exports the router to be used in the application.
module.exports = router;