const userService = require('../services/user');

// Creates a new user using data from the request body and responds with the created user as JSON.
const createUser = async (req, res) => {
    const newUser = await userService.createUser(req.body.username, req.body.email, req.body.password,
        req.body.age, req.body.phoneNumber, req.body.photo, req.body.nickname, req.body.creditcard, req.body.watchedMovies);
    const location = `/api/users/${newUser._id}`;
    res.status(201).location(location).json(newUser);
};

// Retrieves a user by ID. Responds with 404 if the user is not found, otherwise returns the user as JSON.
const getUser = async (req, res, next) => {
    const user = await userService.getUserById(req.params.id);
    if (!user) {
        return res.status(404).json({ errors: ['User not found'] });
    }
    res.status(200).json(user);
    next();
};

// Exports the createUser and getUser functions.
module.exports = { createUser, getUser };