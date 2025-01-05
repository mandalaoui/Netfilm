const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');

const categories = require('./routes/category');
const movies = require('./routes/movie');
const users = require('./routes/user');
const tokens = require('./routes/tokens');
const recommendations = require('./routes/recommend');

require('custom-env').env(process.env.NODE_ENV || 'local', './config');

mongoose.connect(process.env.CONNECTION_STRING);

var app = express();
app.use(cors());
app.use(bodyParser.urlencoded({extended : true}));
app.use(express.json());

app.use('/api/categories', categories);
app.use('/api/movies', movies);
app.use('/api/users', users);
app.use('/api/movies', recommendations);
app.use('/api/tokens', tokens);

app.listen(process.env.PORT);
