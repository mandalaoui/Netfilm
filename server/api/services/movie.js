const Movie = require('../models/movie');
const Category = require('../models/category');
const User = require('../models/user');
const mongoose = require('mongoose');


// Function to create a new movie and add it to the relevant categories.
const createMovie = async (name, categories, movie_time, image, Publication_year, description, age, video, trailer) => {
    const movie = new Movie({ name, categories, movie_time, image, Publication_year, description, age, video, trailer });

    // Add the movie to each category's movie list
    for (const categoryId of categories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies.push(movie._id);
            await category.save();
        }
    }
    // Save the new movie in the database
    return await movie.save();
};

// Function to get a movie by its ID
const getMovieById = async (id) => { 
    const getC = await Movie.findById(id);
    if (!getC) {
        return null;
    }
    return getC;
};

// Function to get movies by categories, filtering out movies already watched by the user. 
// in addition create new category with list of movie the user has watched.
const getMoviesByCategories = async (userId) => { 
    const categories = await Category.find();
    const result = [];
    const user = await User.findById(userId);

    // Get the list of movie IDs the user has watched
    const watchedMovies = user?.watchedMovies.map(id => new mongoose.Types.ObjectId(id)) || [];

    // Loop through promoted categories and find movies that the user hasn't watched yet
    for (const category of categories) {
        if (category.isPromoted) {
            const movies = await Movie.aggregate([
                // Match movies that are not in the user's watched list
                { $match: { categories: category._id, _id: { $nin: watchedMovies } } }, 
                { $sample: { size: 20 } }
            ]);

            // Push the category name and movies to the result array.
            result.push({
                categoryName: category.name,
                movies
            });
        }
    }

    // Get movies the user has watched and add them to the result as "Watch it again" category.
    const watchedCategoryMovies = await Movie.find({ _id: { $in: watchedMovies } })
    .limit(20)
    .exec();

    result.push({
        categoryName: "Watch it again",
        movies: watchedCategoryMovies.sort(() => 0.5 - Math.random())
    });
    return result;
 };

// Function to update an existing movie's details
const updateMovie = async (id, name, categories, movie_time, image, Publication_year, description, age, video, trailer) => {
    const movie = await getMovieById(id);
    if (!movie) return null;

    // Remove the previous movie from the relevant categories.
    const oldcategories = movie.categories;
    for (const categoryId of oldcategories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies = category.movies.filter(catId => String(catId) !== String(movie._id));
            await category.save();
        }
    }

    // Remove the movie from all users' watchedMovies list.
    const users = await User.find({watchedMovies : movie._id }); 

    for (const user of users) {
        user.watchedMovies = user.watchedMovies.filter(movieId => String(movieId) !== String(movie._id)); 
        await user.save();
    }

    // Update the movie's details with the new data
    movie.name = name;
    movie.categories = categories;
    movie.movie_time = movie_time;
    movie.image = image;
    movie.Publication_year = Publication_year;
    movie.description = description;
    movie.age = age;
    movie.video = video;
    movie.trailer = trailer;

    // Add the movie to the new categories
    for (const categoryId of categories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies.push(movie._id);
            await category.save();
        }
    }
    // Save the updated movie
    await movie.save();
    return movie;
};

// Function to delete a movie from the database
const deleteMovie = async (id) => {
    const movie = await getMovieById(id);
    if (!movie) return null;
    
    // Remove the movie from all relevant categories.
    const categories = movie.categories;
    for (const categoryId of categories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies = category.movies.filter(catId => String(catId) !== String(movie._id));
            await category.save();
        }
    }

    // Remove the movie from all users' watchedMovies list
    const users = await User.find({watchedMovies : movie._id }); 

    for (const user of users) {
        user.watchedMovies = user.watchedMovies.filter(movieId => String(movieId) !== String(movie._id)); 
        await user.save();
    }

    // Delete the movie from the database
    await Movie.deleteOne({ _id: id });
    return movie;
};

// Function to search for movies by name or description using a query
const movieIncludeQuery =  async (query) => {
    const movies = await Movie.find({
            $or: [
                { name: { $regex: `\\b${query}\\b`, $options: 'i' } },     
                { description: { $regex: `\\b${query}\\b`, $options: 'i' } },
            ]
        });
    return movies;
}

// Exporting all functions to be used in the service layer.
module.exports = {createMovie, getMovieById, updateMovie, deleteMovie, getMoviesByCategories, movieIncludeQuery }