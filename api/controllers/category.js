const categoryService = require('../services/category');

// Handler to create a new category
const createCategory = async (req, res) => {
    res.json(await categoryService.createCategory(req.body.name, req.body.isPromoted, req.body.movies));
};

// Handler to fetch all categories
const getCategories = async (req, res) => {
    res.json(await categoryService.getCategories());
};

// Handler to fetch a category by its ID
const getCategory = async (req, res) => {
    const category = await categoryService.getCategoryById(req.params.id);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.json(category);
};

// Handler to update an existing category by its ID
const updateCategory = async (req, res) => {
    const category = await categoryService.updateCategory(req.params.id, req.body.name, req.body.isPromoted, req.body.movies);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.json(category);
};

// Handler to delete a category by its ID
const deleteCategory = async (req, res) => {
    const category = await categoryService.deleteCategory(req.params.id);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.json(category);
};

module.exports = {createCategory, getCategories, getCategory, updateCategory, deleteCategory };