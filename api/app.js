const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');
const categories = require('./routes/category');

require('custom-env').env(process.env.NODE_ENV, './config');
mongoose.connect('mongodb://127.0.0.1:27017/api');

var app = express();
app.use(cors());
app.use(bodyParser.urlencoded({extended : true}));
app.use(express.json());
app.use('/categories', categories);
app.listen(process.env.PORT);