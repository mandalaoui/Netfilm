const Category = require('../models/category');
const Movie = require('../models/movie');

//router.route('/')
const createCategory = async (name, isPromoted, movies) => {
    const category = new Category({ name, isPromoted, movies });
    // Update the list of categories in the relevant movies 
    for (const movieId of movies) {
        const movie = await Movie.findById(movieId);
        if (movie) {
            movie.categories.push(category._id);
            await movie.save();
        }
    }
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

const updateCategory = async (id, name, isPromoted, movies) => {
    const category = await getCategoryById(id);
    if (!category) return null;

    // Remove the previous category from the relevant movies 
    const oldMovies = category.movies;
    for (const movieId of oldMovies) {
        const movie = await Movie.findById(movieId);
        if (movie) {
            movie.categories = movie.categories.filter(catId => !catId.equals(category._id));
            await movie.save();
        }
    }
    // Update the category
    category.name = name;
    category.isPromoted = isPromoted;
    category.movies = movies;
    // Add this category to the list of categories in the relevant movies
    for (const movieId of movies) {
        const movie = await Movie.findById(movieId);
        if (movie) {
            movie.categories.push(category._id);
            await movie.save();
        }
    }
    await category.save();
    return category;
};

const deleteCategory = async (id) => {
    const category = await getCategoryById(id);
    if (!category) return null;
    // Remove the category from the relevant movies 
    const movies = category.movies;
    for (const movieId of movies) {
        const movie = await Movie.findById(movieId);
        if (movie) {
            movie.categories = movie.categories.filter(catId => String(catId) !== String(category._id));
            await movie.save();
        }
    }
    await Category.deleteOne({ _id: id });
    return category;
};
module.exports = {createCategory, getCategoryById, getCategories, updateCategory, deleteCategory }