const Category = require('../models/category');
const mongoose = require('mongoose');


const validateMovieInput = async (req, res, next) => {
    const { name, categories, movie_time, Publication_year, age } = req.body;

    if (!name || typeof name !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing name' });
    }

    if (!Array.isArray(categories)) {
        return res.status(400).json({ error: 'Invalid or missing categories' });
    }

    if (!movie_time || typeof movie_time !== 'string') {
        return res.status(400).json({ error: 'Invalid or missing movie_time' });
    }

    if (!Publication_year || typeof Publication_year !== 'number') {
        return res.status(400).json({ error: 'Invalid or missing Publication_year' });
    }

    if (age && typeof age !== 'number') {
        return res.status(400).json({ error: 'Age must be a number' });
    }

    for (const categoryId of categories) {
        if (!mongoose.Types.ObjectId.isValid(categoryId)) {
            return res.status(400).json({ error: `Invalid category ID: ${categoryId}` });
        }
    }

    const categoryCheck = await Category.find({ '_id': { $in: categories } });
    if (categoryCheck.length !== categories.length) {
        return res.status(404).json({ error: 'One or more categories do not exist' });
    }

    next();
};

module.exports = { validateMovieInput };
