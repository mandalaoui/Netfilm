const tokensService = require('../services/tokens');
const jwt = require('jsonwebtoken');

// Handles user authentication by verifying the provided username and password
const autentication = async (req, res, next) => {
    // Validate the existence of the user by checking the provided credentials
    const user = await tokensService.isExist(req.body.username, req.body.password);
    // If no user is found, return a 404 error with a "User not found" message
    if (!user) {
        return res.status(404).json({ errors: ['Username and/or password incorrect'] });
    }
    // If authentication is successful, respond with the user's ID
    const token = generateToken(user);
    res.status(200).json({ userId: user._id, token });

    next();
};

const generateToken = (user) => {
    const secret = process.env.JWT_SECRET;
    if (!secret) {
      throw new Error('JWT secret is not defined');
    }
  
    return jwt.sign(
      {
        user_name: user.user_name, 
        mail: user.mail,           
        phone: user.phone,         
        picture: user.picture,
        manager: user.manager
      },
      secret,
      { expiresIn: '24h' } 
    );
};

module.exports = {autentication};