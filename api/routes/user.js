const express = require('express');
var router = express.Router();
const userController = require('../controllers/user');
const validUser = require('../validation/user')

router.route('/')
    .post(validUser.validateUserInput, userController.createUser);

router.route('/:id')
    .get(userController.getUser)

module.exports = router;