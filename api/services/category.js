const Category = require('../models/category');

//router.route('/')
const createCategory = async (name, promoted, movies) => {
    const category = new Category({ name, promoted, movies });
    return await category.save();
};

const getCategories = async () => { return await Category.find({}); };

// router.route('/:id')
const getCategoryById = async (id) => { 
    const getC = await Category.findById(id);
    if (!getC) {
        return null;
    }
    return getC;
};

const updateCategory = async (id, name, promoted, movies) => {
    const category = await getCategoryById(id);
    if (!category) return null;
    category.name = name;
    category.promoted = promoted;
    category.movies = movies;
    await category.save();
    return category;
};

const deleteCategory = async (id) => {
    const category = await getCategoryById(id);
    if (!category) return null;
    await Category.deleteOne({ _id: id });
    return category;
};
module.exports = {createCategory, getCategoryById, getCategories, updateCategory, deleteCategory }