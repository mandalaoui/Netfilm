const express = require('express');
var router = express.Router();
const userController = require('../controllers/user');
const validUser = require('../validation/user')

// Route for creating a new user. Validates input before calling the controller's createUser function.
router.route('/')
    .post(validUser.validateUserInput, userController.createUser);

// Route for retrieving a user by their ID. Calls the controller's getUser function.
router.route('/:id')
    .get(userController.getUser)

// Exports the router to be used in the application.
module.exports = router;