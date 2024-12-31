const User = require('../models/user');

const createUser = async (username, email, password, age, phoneNumber, photo, nickname, creditcard, watchedMovies) => {
    const user = new User({ username, email, password, age, phoneNumber, photo, nickname, creditcard, watchedMovies });
    return await user.save();
};

const getUserById = async (id) => { 
    const getC = await User.findById(id);
    if (!getC) {
        return null;
    }
    return getC;
};

module.exports = {createUser, getUserById }