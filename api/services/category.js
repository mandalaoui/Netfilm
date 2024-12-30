const Category = require('../models/category');

const createCategory = async (name, description) => {
    const category = new Category({ name, description });
    return await category.save();
};

const getCategoryById = async (id) => { 
    const getC = await Category.findById(id);
    if (!getC) {
        return null;
    }
    return getC;
};
const getCategories = async () => { return await Category.find({}); };

const updateCategory = async (id, name, description) => {
    const category = await getCategoryById(id);
    if (!category) return null;
    category.name = name;
    category.description = description;
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