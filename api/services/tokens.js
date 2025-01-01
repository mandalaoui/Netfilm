const User = require('../models/user');

const isExist = async (username, password) => {
    const user = await User.findOne({ username : username, password : password });
    if (user) {
        return {userId : user.id }
    }
};

module.exports = {isExist}