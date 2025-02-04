const User = require('../models/user');

// Checks if a user exists in the database based on the provided username and password
const isExist = async (username, password) => {
    const user = await User.findOne({ username : username, password : password });
    if (user) {
        return {
            id: user._id, 
            username: user.username, 
            isAdmin: user.isAdmin 
        };
    }
    return null;
};

module.exports = {isExist}