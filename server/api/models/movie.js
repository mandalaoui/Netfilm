
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
        required: false
    }],
    movie_time: {
        type: String,
        required: true
    },
    image: {
        type: String,
        required: true
    },
    Publication_year: {
        type: Number,
        required: true
    },
    description: {
        type: String,
        default: ''
    },
    video: {
        type: String,
        required: false
    },
    video: {
        type: String,
        required: true
    },
    trailer: {
        type: String,
        required: true
    }
    // trailer: {
    //     type: String,
    //     required: false
    // },
    // age: {
    //     type: Number,
    //     required: false
    // }
});

// Export the Movie model using the schema defined above
module.exports = mongoose.model('Movie', Movie);