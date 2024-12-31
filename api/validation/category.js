// const Category = require('../models/category');
const Movie = require('../models/movie');

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
    // Check if all movies exist in the database
    const foundMovies = await Movie.find({ _id: { $in: movies } });
    if (foundMovies.length !== movies.length) {
        return res.status(404).json({ error: 'One or more movies do not exist' });
    }

    // If all validations pass
    next();
}

module.exports = { validateCategoryInput };
