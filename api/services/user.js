const User = require('../models/user');

// Creates a new user and saves it to the database using the provided parameters.
const createUser = async (username, email, password, age, phoneNumber, photo, nickname, creditcard, watchedMovies) => {
    const user = new User({ username, email, password, age, phoneNumber, photo, nickname, creditcard, watchedMovies });
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

// Exports the createUser and getUserById functions to be used in other parts of the application.
module.exports = {createUser, getUserById }