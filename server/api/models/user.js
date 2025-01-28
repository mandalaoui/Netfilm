const mongoose = require('mongoose');

const Schema = mongoose.Schema;

// Defines the schema for the User collection in the database.
const User = new Schema({
    username : {
        type: String,
        required: true
    },
    password : {
        type: String,
        required: true
    },
    photo : {
        type: String,
        required: false
    },
    nickname : {
        type: String,
        required: true
    },
    watchedMovies : [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Movie',
        default: ''
    }],
    isAdmin : {
        type: Boolean,
        required: true
    }
});

// Exports the User model to be used in other parts of the application.
module.exports = mongoose.model('User', User);