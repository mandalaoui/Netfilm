const Category = require('../models/category');
const Movie = require('../models/movie');
const mongoose = require('mongoose');

// Middleware to validate movie input when creating or updating a movie.
const validateMovieInput = async (req, res, next) => {
    const { name, categories, movie_time, Publication_year, age, imageUrl} = req.body;

    let categoriesChange = [];

    if (categories) {
        if (typeof categories === 'string') {
            categoriesChange = categories.split(',').map(categoryId => new mongoose.Types.ObjectId(categoryId));
        } else if (Array.isArray(categories)) {
            categoriesChange = categories;
        }
    }


    // Check if the 'name' field is provided and is a string
    if (!name || typeof name !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing name' });
    }

    // Check if 'categories' is an array
    if (!Array.isArray(categoriesChange)) {
        return res.status(400).json({ error: 'Invalid or missing categories' });
    }

    // Check if 'movie_time' is provided and is a string
    if (!movie_time || typeof movie_time !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing movie_time' });
    }

    // Check if 'movie_time' format (xx:xx)
    const timeRegex = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/; // Supports 00:00 to 23:59
    if (!timeRegex.test(movie_time)) {
        return res.status(400).json({ error: 'Invalid movie_time format. Must be in HH:MM format (e.g., 13:45)' });
    }

    // check Validate 'Publication_year' ,must not be greater than the current year.
    const currentYear = new Date().getFullYear();
    if (Publication_year > currentYear) {
        return res.status(400).json({ error: `Invalid Publication_year. Must be less than or equal to the current year (${currentYear})` });
    }

    // Check if 'age' is a number if provided
    if (age !== undefined) {
        if (age > 18) {
            return res.status(400).json({ error: 'Age cannot be greater than 18' });
        }
        if (age % 2 !== 0) {
            return res.status(400).json({ error: 'Age must be an even number' });
        }
    }

    // Check if validate 'imageUrl' format (if provided).
    if (imageUrl) {
        if (typeof imageUrl !== 'string') {
            return res.status(400).json({ error: 'Invalid image URL. Must be a string' });
        }

        const imageRegex = /\.(jpeg|jpg|gif|png|webp|bmp|svg)$/i; // Common image formats
        if (!imageRegex.test(imageUrl)) {
            return res.status(400).json({ error: 'Invalid image URL format. Must end with an image file extension (e.g., .jpg, .png)' });
        }
    }

    // Validate each category ID in the 'categories' array
    for (const categoryId of categoriesChange) {
        if (!mongoose.Types.ObjectId.isValid(categoryId)) {
            return res.status(400).json({ error: `Invalid category ID: ${categoryId}` });
        }
    }

    // Check if a movie with the exact same fields already exists.
    const existingMovie = await Movie.findOne({
        name,
        ...(categoriesChange.length > 0 && { categoriesChange: { $all: categoriesChange.sort(), $size: categoriesChange.length } }),
        movie_time,
        Publication_year,
    });

    if (existingMovie) {
        const method = req.method;
        if (method === 'POST')
            return res.status(404).json({ error: 'A movie with the exact same details already exists' });
    }

    // Check if the provided categories exist in the database
    const categoryCheck = await Category.find({ '_id': { $in: categoriesChange } });
    if (categoryCheck.length !== categoriesChange.length) {
        return res.status(404).json({ error: 'One or more categories do not exist' });
    }

    next();
};


// Middleware to validate movie ID in URL params
const validateMovieId = async (req, res, next) => {
    const { id } = req.params;
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

// Export the validation middleware functions
module.exports = { validateMovieInput, validateMovieId };
