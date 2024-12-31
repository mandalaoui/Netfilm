const userService = require('../services/user');

const createUser = async (req, res) => {
    res.json(await userService.createUser(req.body.username, req.body.email, req.body.password,
        req.body.age, req.body.phoneNumber, req.body.photo, req.body.nickname, req.body.creditcard, req.body.watchedMovies));
};

const getUser = async (req, res) => {
    const user = await userService.getUserById(req.params.id);
    if (!user) {
        return res.status(404).json({ errors: ['404 Not Found'] });
    }
    res.json(user);    
};

module.exports = {createUser, getUser };