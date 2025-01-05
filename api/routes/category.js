const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const categoryValidation = require('../validation/category');


router.route('/')
    .get(categoryValidation.validateUserId, categoryController.getCategories)
    .post(categoryValidation.validateUserId, categoryValidation.validateCategoryInput, categoryController.createCategory);

router.route('/:id')
    .get(categoryValidation.validateUserId, categoryValidation.validateCategoryId, categoryController.getCategory)
    .patch(categoryValidation.validateUserId, categoryValidation.validateCategoryId, categoryValidation.validateCategoryInput, categoryController.updateCategory)
    .delete(categoryValidation.validateUserId, categoryValidation.validateCategoryId, categoryController.deleteCategory);

module.exports = router;