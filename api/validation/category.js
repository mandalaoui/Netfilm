const Category = require('../models/category');
const Movie = require('../models/movie');
const mongoose = require('mongoose');

// Middleware to validate the category input
const validateCategoryInput = async (req, res, next) => {
    const { name, isPromoted, movies } = req.body;

    // Validate 'name'
    if (!name || typeof name !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing name'})
    }
    // Validate 'isPromoted'
    if (typeof isPromoted !== 'boolean') {
        return res.status(400).json({ error: 'Invalid or missing isPromoted value' });
    }
    // Validate 'movies'
    if (!Array.isArray(movies)) {
        return res.status(400).json({ error: 'Movies must be an array' });
    }

    // Validate each movie ID in the 'movies' array
    for (const movieId of movies) {
        if (!mongoose.Types.ObjectId.isValid(movieId)) {
            return res.status(400).json({ error: `Invalid movie ID: ${movieId}` });
        }
    }
    // Check if all movies exist in the database
    const foundMovies = await Movie.find({ _id: { $in: movies } });
    if (foundMovies.length !== movies.length) {
        return res.status(404).json({ error: 'One or more movies do not exist' });
    }

    // If all validations pass
    next();
}

// Middleware to validate the category ID in the request parameters
const validateCategoryId = async (req, res, next) => {
    const { id } = req.params;

    // Validate the 'id' field: It must be a valid MongoDB ObjectId
    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).json({ error: 'Invalid category ID' });
    }

    // Check if the category exists in the database
    const category = await Category.findById(id);
    if (!category) {
        return res.status(404).json({ error: 'Category not found' });
    }

    // If the category exists, proceed to the next middleware or route handler
    next();
};

module.exports = { validateCategoryInput, validateCategoryId };
