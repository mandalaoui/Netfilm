const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Movie = new Schema({
    name : {
        type: String,
        required: true
    },
    categories: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Category',
        default: ''
    }],
    movie_time: {
        type: String,
        required: true
    },
    image: {
        type: String,
        required: false
    },
    Publication_year: {
        type: String,
        required: true
    },
    description: {
        type: String,
        default: ''
    },
    age: {
        type: String,
        required: false
    }
});

module.exports = mongoose.model('Movie', Movie);