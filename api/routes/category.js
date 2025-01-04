const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const categoryValidation = require('../validation/category');


router.route('/')
    .get(categoryController.getCategories)
    .post(categoryValidation.validateCategoryInput, categoryController.createCategory);

router.route('/:id')
    .get(categoryValidation.validateCategoryId, categoryController.getCategory)
    .patch(categoryValidation.validateCategoryId, categoryValidation.validateCategoryInput, categoryController.updateCategory)
    .delete(categoryValidation.validateCategoryId, categoryController.deleteCategory);

module.exports = router;