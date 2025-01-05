const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const categoryValidation = require('../validation/movie');
const { validateCategoryInput } = require('../validation/category');


router.route('/')
    .get(categoryValidation.validateUserId, categoryController.getCategories)
    .post(validateCategoryInput, categoryValidation.validateUserId, categoryController.createCategory);

router.route('/:id')
    .get(categoryValidation.validateUserId, categoryController.getCategory)
    .patch(validateCategoryInput, categoryValidation.validateUserId, categoryController.updateCategory)
    .delete(validateCategoryInput, categoryValidation.validateUserId, categoryController.deleteCategory);

module.exports = router;