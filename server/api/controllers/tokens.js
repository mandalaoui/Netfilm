const tokensService = require('../services/tokens');

// Handles user authentication by verifying the provided username and password
const autentication = async (req, res, next) => {
    // Validate the existence of the user by checking the provided credentials
    const userId = await tokensService.isExist(req.body.username, req.body.password);
    // If no user is found, return a 404 error with a "User not found" message
    if (!userId) {
        return res.status(404).json({ errors: ['Username and/or password incorrect'] });
    }
    // If authentication is successful, respond with the user's ID
    res.status(200).json(userId);
    next();
};

module.exports = {autentication};