const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const categoryValidation = require('../validation/category');
const userValidation = require('../validation/user');


// Define routes for '/'
router.route('/')
    .get(userValidation.validateUserId, categoryController.getCategories)
    .post(userValidation.validateUserId, categoryValidation.validateCategoryInput, categoryController.createCategory);

// Define routes for '/:id'
router.route('/:id')
    .get(userValidation.validateUserId, categoryValidation.validateCategoryId, categoryController.getCategory)
    .patch(userValidation.validateUserId, categoryValidation.validateCategoryId, categoryValidation.validateCategoryInput, categoryController.updateCategory)
    .delete(userValidation.validateUserId, categoryValidation.validateCategoryId, categoryController.deleteCategory);

module.exports = router;