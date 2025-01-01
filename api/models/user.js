const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const User = new Schema({
    username : {
        type: String,
        required: true
    },
    email : {
        type: String,
        required: true
    },
    password : {
        type: String,
        required: true
    },
    age : {
        type: String,
        required: true
    },
    phoneNumber : {
        type: String,
        required: true
    },
    photo : {
        type: Buffer,
        required: false
    },
    nickname : {
        type: String,
        required: false
    },
    creditcard : {
        type: String,
        required: true  
    },
    watchedMovies : [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Movie',
        default: ''
    }]
});

module.exports = mongoose.model('User', User);