const Movie = require('../models/movie');
const Category = require('../models/category');
const User = require('../models/user');


const createMovie = async (name, categories, movie_time, image, Publication_year, description, age) => {
    const movie = new Movie({ name, categories, movie_time, image, Publication_year, description, age });
    for (const categoryId of categories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies.push(movie._id);
            await category.save();
        }
    }
    return await movie.save();
};

const getMovieById = async (id) => { 
    const getC = await Movie.findById(id);
    if (!getC) {
        return null;
    }
    return getC;
};

//const getMovies = async () => { return await Movie.find({}); };
const getMoviesByCategories = async (userId) => { 
    const categories = await Category.find();
    const result = [];
    const user = await User.findById(userId);
    const watchedMovies = user?.watchedMovies || [];

    for (const category of categories) {
        if (category.isPromoted) {
            const movies = await Movie.aggregate([
                { $match: { categories: category._id, _id: { $nin: watchedMovies } } }, 
                { $sample: { size: 20 } }
            ]);

            result.push({
                categoryName: category.name,
                movies
            });
        }
    }
    const watchedCategoryMovies = await Movie.find({ _id: { $in: watchedMovies } })
    .limit(20)
    .exec();

    result.push({
        categoryName: "Watch it again",
        movies: watchedCategoryMovies.sort(() => 0.5 - Math.random())
    });
    return result;
 };


const updateMovie = async (id, name, categories, movie_time, image, Publication_year, description, age) => {
    const movie = await getMovieById(id);
    if (!movie) return null;

    // Remove the previous movie from the relevant category
    const oldcategories = movie.categories;
    for (const categoryId of oldcategories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies = category.movies.filter(catId => String(catId) !== String(movie._id));
            await category.save();
        }
    }
    
    const users = await User.find({watchedMovies : movie._id }); 

    for (const user of users) {
        user.watchedMovies = user.watchedMovies.filter(movieId => String(movieId) !== String(movie._id)); 
        await user.save();
    }

    movie.name = name;
    movie.categories = categories;
    movie.movie_time = movie_time;
    movie.image = image;
    movie.Publication_year = Publication_year;
    movie.description = description;
    movie.age = age;

    for (const categoryId of categories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies.push(movie._id);
            await category.save();
        }
    }
    await movie.save();
    return movie;
};

const deleteMovie = async (id) => {
    const movie = await getMovieById(id);
    if (!movie) return null;

    const categories = movie.categories;
    for (const categoryId of categories) {
        const category = await Category.findById(categoryId);
        if (category) {
            category.movies = category.movies.filter(catId => String(catId) !== String(movie._id));
            await category.save();
        }
    }
    const users = await User.find({watchedMovies : movie._id }); 

    for (const user of users) {
        user.watchedMovies = user.watchedMovies.filter(movieId => String(movieId) !== String(movie._id)); 
        await user.save();
    }

    await Movie.deleteOne({ _id: id });
    return movie;
};

const movieIncludeQuery =  async (query) => {
    const movies = await Movie.find({
            $or: [
                { name: { $regex: query, $options: 'i' } },     
                { description: { $regex: query, $options: 'i' } },
            ]
        });
    return movies;
}

module.exports = {createMovie, getMovieById, updateMovie, deleteMovie, getMoviesByCategories, movieIncludeQuery }