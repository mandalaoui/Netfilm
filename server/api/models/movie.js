const mongoose = require('mongoose');

const Schema = mongoose.Schema;

// Define the Movie schema
const Movie = new Schema({
    name : {
        type: String,
        required: true
    },
    categories: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Category',
    }],
    movie_time: {
        type: String,
        required: true
    },
    image: {
        type: mongoose.Schema.Types.ObjectId,
        required: false
    },
    Publication_year: {
        type: Number,
        required: true
    },
    description: {
        type: String,
        default: ''
    },
    age: {
        type: Number,
        required: false
    },
    video: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    },
    trailer: {
        type: mongoose.Schema.Types.ObjectId,
        required: true
    }
});

// Export the Movie model using the schema defined above
module.exports = mongoose.model('Movie', Movie);