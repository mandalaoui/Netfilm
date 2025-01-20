const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const categoryValidation = require('../validation/category');
const userValidation = require('../validation/user');


// Define routes for '/'
router.route('/')
    .get(userValidation.validateUserIdHeader, categoryController.getCategories)
    .post(userValidation.validateUserIdHeader, categoryValidation.validateCategoryInput, categoryController.createCategory);

// Define routes for '/:id'
router.route('/:id')
    .get(userValidation.validateUserIdHeader, categoryValidation.validateCategoryId, categoryController.getCategory)
    .patch(userValidation.validateUserIdHeader, categoryValidation.validateCategoryId, categoryValidation.validateCategoryInput, categoryController.updateCategory)
    .delete(userValidation.validateUserIdHeader, categoryValidation.validateCategoryId, categoryController.deleteCategory);

module.exports = router;