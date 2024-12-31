const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const Category = new Schema({
    name : {
        type: String,
        required: true
    },
    isPromoted : {
        type: Boolean,
        required: true
    },
    movies: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Movie',
        // default: ''
    }]
});

module.exports = mongoose.model('Category', Category);