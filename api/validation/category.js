// const Category = require('../models/category');
const Movie = require('../models/movie');
const mongoose = require('mongoose');
const userService = require('../services/user');


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

module.exports = { validateCategoryInput, validateUserId };
