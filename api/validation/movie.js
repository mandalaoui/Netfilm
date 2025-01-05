const Category = require('../models/category');
const mongoose = require('mongoose');
const userService = require('../services/user');


// Middleware to validate movie input when creating or updating a movie.
const validateMovieInput = async (req, res, next) => {
    const { name, categories, movie_time, Publication_year, age } = req.body;

    // Check if the 'name' field is provided and is a string
    if (!name || typeof name !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing name' });
    }

    // Check if 'categories' is an array
    if (!Array.isArray(categories)) {
        return res.status(400).json({ error: 'Invalid or missing categories' });
    }

    // Check if 'movie_time' is provided and is a string
    if (!movie_time || typeof movie_time !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing movie_time' });
    }

    // Check if 'Publication_year' is provided and is a number
    if (!Publication_year || typeof Publication_year !== 'number') {
        return res.status(400).json({ error: 'Invalid or missing Publication_year' });
    }

    // Check if 'age' is a number if provided
    if (age && typeof age !== 'number') {
        return res.status(400).json({ error: 'Age must be a number' });
    }

    // Validate each category ID in the 'categories' array
    for (const categoryId of categories) {
        if (!mongoose.Types.ObjectId.isValid(categoryId)) {
            return res.status(400).json({ error: `Invalid category ID: ${categoryId}` });
        }
    }

    // Check if the provided categories exist in the database
    const categoryCheck = await Category.find({ '_id': { $in: categories } });
    if (categoryCheck.length !== categories.length) {
        return res.status(404).json({ error: 'One or more categories do not exist' });
    }

    next();
};

// Middleware to validate movie ID in URL params
const validateMovieId = async (req, res, next) => {
    const id = req.param.id;

    // Check if the provided movie ID is a valid ObjectId
    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).json({ error: ['Invalid movie ID'] });
    }

    // Check if the movie exists in the database
    const movie = await Movie.findById(id);
    if (!movie) {
        return res.status(404).json({ error: ['Movie not found'] });
    }
    next();
}

// Middleware to validate the `userId` in the request header
const validateUserId = async (req, res, next) => {
    const userID = req.header('userId');

    // Check if the userId is missing in the request header
    if(!userID) {
        return res.status(404).json({ errors: ['User must be conected'] });
    }

    // Fetch the user from the database using the userId
    const user = await userService.getUserById(userID);

    // If no user is found, return a "User not found" error
    if(!user) {
        return res.status(404).json({ errors: ['User not found'] });
    }

    // Check if the userId is in a valid MongoDB ObjectId format
    if (!mongoose.Types.ObjectId.isValid(userId)) {
        return res.status(400).json({ error: ['Invalid User ID format'] });
    }

    next();
}

// Export the validation middleware functions
module.exports = { validateMovieInput, validateMovieId, validateUserId };
