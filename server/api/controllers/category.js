const categoryService = require('../services/category');

// Handler to create a new category
const createCategory = async (req, res, next) => {
    const newCategory = await categoryService.createCategory(req.body.name, req.body.isPromoted, req.body.movies);
    const location = `/api/categories/${newCategory._id}`;
    res.status(201).location(location).json(newCategory);
    
    next();
};

// Handler to fetch all categories
const getCategories = async (req, res, next) => {
    res.status(200).json(await categoryService.getCategories());
    next();
};

// Handler to fetch a category by its ID
const getCategory = async (req, res, next) => {
    const category = await categoryService.getCategoryById(req.params.id);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.status(200).json(category);
    next();
};

// Handler to update an existing category by its ID
const updateCategory = async (req, res, next) => {
    const category = await categoryService.updateCategory(req.params.id, req.body.name, req.body.isPromoted, req.body.movies);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.status(204).json(category);
    next();
};

// Handler to delete a category by its ID
const deleteCategory = async (req, res, next) => {
    const category = await categoryService.deleteCategory(req.params.id);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.status(204).json({});
    next();
};

module.exports = {createCategory, getCategories, getCategory, updateCategory, deleteCategory };