const Movie = require('../models/movie');
const Category = require('../models/category');
const User = require('../models/user');


const createMovie = async (name, categories, movie_time, image, Publication_year, description, age) => {
    const movie = new Movie({ name, categories, movie_time, image, Publication_year, description, age });
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
    movie.name = name;
    movie.categories = categories;
    movie.movie_time = movie_time;
    movie.image = image;
    movie.Publication_year = Publication_year;
    movie.description = description;
    movie.age = age;
    await movie.save();
    return movie;
};

const deleteMovie = async (id) => {
    const movie = await getMovieById(id);
    if (!movie) return null;
    await Movie.deleteOne({ _id: id });
    return movie;
};

module.exports = {createMovie, getMovieById, getMoviesByCategories, updateMovie, deleteMovie }