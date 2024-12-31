const categoryService = require('../services/category');

// router.route('/')
const createCategory = async (req, res) => {
    res.json(await categoryService.createCategory(req.body.name, req.body.isPromoted, req.body.movies));
};

const getCategories = async (req, res) => {
    res.json(await categoryService.getCategories());
};

// router.route('/:id')
const getCategory = async (req, res) => {
    const category = await categoryService.getCategoryById(req.params.id);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.json(category);
};

const updateCategory = async (req, res) => {
    const category = await categoryService.updateCategory(req.params.id, req.body.name, req.body.isPromoted, req.body.movies);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.json(category);
};

const deleteCategory = async (req, res) => {
    const category = await categoryService.deleteCategory(req.params.id);
    if (!category) {
        return res.status(404).json({ errors: ['Category not found'] });
    }
    res.json(category);
};

module.exports = {createCategory, getCategories, getCategory, updateCategory, deleteCategory };