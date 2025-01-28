const tokensService = require('../services/tokens');
const jwt = require('jsonwebtoken');
require('custom-env').env(process.env.NODE_ENV || 'local', './config');

// Handles user authentication by verifying the provided username and password
const autentication = async (req, res, next) => {
    try {
        // Validate the existence of the user by checking the provided credentials
        const user = await tokensService.isExist(req.body.username, req.body.password);

        // If no user is found, return a 404 error with a "User not found" message
        if (!user) {
            return res.status(404).json({ errors: ['Username and/or password incorrect'] });
        }

        // Generate a token with the user's details
        const token = generateToken(user);

        // Respond with the token and userId
        res.status(200).json({ userId: user._id, token });
    } catch (error) {
        console.error('Authentication error:', error);
        res.status(500).json({ errors: ['Internal server error'] });
    }
};

const generateToken = (user) => {
    const jwtSecret = process.env.JWT_KEY;
    if (!jwtSecret) {
      throw new Error('JWT secret is not defined');
    }
  
    return jwt.sign(
        {
            id: user.id, 
            username: user.username, 
            isAdmin: user.isAdmin,
        },
        jwtSecret,
        { expiresIn: '24h' } 
    );
};

module.exports = {autentication};