const User = require('../models/user');
const userService = require('../services/user');
const mongoose = require('mongoose');


const validateUserInput = async (req, res, next) => {
    const {
        username,
        password,
        photo,
        nickname,
        watchedMovies
    } = req.body;

    // Validate username: must be a non-empty string.
    if (!username || typeof username !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing username' });
    }

    // Check if the username is unique in the database
    const existingUser = await User.findOne({ username });
    if (existingUser) {
        return res.status(404).json({ error: 'Username already exists' });
    }

    // Validate password: must be at least 6 characters long.
    if (!password || password.length < 6) {
        return res.status(400).json({ error: 'Password must be at least 6 characters long' });
    }

    // Validate photo: must be URL format, if provided.
    if (photo) {
        if (typeof photo !== 'string') {
            return res.status(400).json({ error: 'Invalid image URL. Must be a string' });
        }

        const imageRegex = /\.(jpeg|jpg|gif|png|webp|bmp|svg)$/i; // Common image formats
        if (!imageRegex.test(photo)) {
            return res.status(400).json({ error: 'Invalid image URL format. Must end with an image file extension (e.g., .jpg, .png)' });
        }
    }    

    // Validate nickname: must be a string, if provided.
    if (nickname && typeof nickname !== 'string') {
        return res.status(400).json({ error: 'Invalid nickname format' });
    }

    // If all validations pass, proceed to the next middleware or route handler.
    next();
};

// Middleware to validate the `userId` in the request header
const validateUserIdHeader = async (req, res, next) => {
    const userID = req.header('userId');

    // Check if the userId is missing in the request header
    if(!userID) {
        return res.status(400).json({ errors: ['User must be conected'] });
    }

    // Check if the userId is in a valid MongoDB ObjectId format
    if (!mongoose.Types.ObjectId.isValid(userID)) {
        return res.status(400).json({ error: ['Invalid User ID format'] });
    }

    // Fetch the user from the database using the userId
    const user = await userService.getUserById(userID);

    // If no user is found, return a "User not found" error
    if(!user) {
        return res.status(404).json({ errors: ['User not found'] });
    }

    next();
}

// Middleware to validate the user ID in the request parameters
const validateUserId = async (req, res, next) => {
    const { id } = req.params;

    // Validate the 'id' field: It must be a valid MongoDB ObjectId
    if (!mongoose.Types.ObjectId.isValid(id)) {
        return res.status(400).json({ error: 'Invalid user ID' });
    }

    // Check if the category exists in the database
    const user = await User.findById(id);
    if (!user) {
        return res.status(404).json({ error: 'User not found' });
    }

    // If the category exists, proceed to the next middleware or route handler
    next();
};

// Export the validation function for use in routes or controllers.
module.exports = { validateUserInput, validateUserIdHeader, validateUserId};
