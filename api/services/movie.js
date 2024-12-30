const Movie = require('../models/movie');

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
const getMovies = async () => { return await Movie.find({}); };

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

module.exports = {createMovie, getMovieById, getMovies, updateMovie, deleteMovie }