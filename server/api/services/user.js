const User = require('../models/user');
const Movie = require('../models/movie');


// Creates a new user and saves it to the database using the provided parameters.
const createUser = async (username, password, photo, nickname, watchedMovies, isAdmin) => {
    const user = new User({ username, password, photo, nickname, watchedMovies, isAdmin });
    return await user.save();
};

// Retrieves a user from the database by their ID.
const getUserById = async (id) => { 
    const getU = await User.findById(id);
    if (!getU) {
        return null;
    }
    return getU;
};

// Adds a movie to the user's watchlist.
const addToWatchList = async (userId, movieId) => { 
    try {
        // Find the user by their ID in the database and populate the watchedMovies field
        const user = await User.findById(userId);
        // Check if the movie is already in the user's watchlist
        if (user.watchedMovies.includes(movieId)) {
            return "Movie is already in the watchlist";
        }
        // Add the movie to the user's watchlist
        user.watchedMovies.push(movieId);
        await user.save();
        // Return the updated watchlist
        return await Movie.findById(movieId);;
    } catch (error) {
        // Log an error message if something goes wrong
        console.error('Error adding movie to watchlist:', error);
        return null;
    }
};


// Exports the createUser and getUserById functions to be used in other parts of the application.
module.exports = {createUser, getUserById, addToWatchList }