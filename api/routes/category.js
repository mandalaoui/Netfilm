const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const { validateCategoryInput } = require('../validation/category');


router.route('/')
    .get(categoryController.getCategories)
    .post(validateCategoryInput, categoryController.createCategory);

router.route('/:id')
    .get(categoryController.getCategory)
    .patch(validateCategoryInput, categoryController.updateCategory)
    .delete(validateCategoryInput, categoryController.deleteCategory);

module.exports = router;