const User = require('../models/user');

// Checks if a user exists in the database based on the provided username and password
const isExist = async (username, password) => {
    const user = await User.findOne({ username : username, password : password });
    if (user) {
        return {userId : user.id }
    }
    return null;
};

module.exports = {isExist}