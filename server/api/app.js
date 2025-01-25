// Importing required modules
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');
// const multer = require('multer');


// const storage = multer.diskStorage({
//     destination: function (req, file, cb) {
//         cb(null, 'uploads/'); // תיקיית יעד לשמירה על הקבצים
//     },
//     filename: function (req, file, cb) {
//         cb(null, Date.now() + '-' + file.originalname); // שם הקובץ יהיה כולל תאריך כדי למנוע התנגשויות
//     }
// });

// // הגדרת middleware של multer, המתיר לקבל תמונות בלבד
// const upload = multer({
//     storage: storage,
//     fileFilter: function (req, file, cb) {
//         const filetypes = /jpeg|jpg|png/;
//         const mimetype = filetypes.test(file.mimetype);
//         const extname = filetypes.test(file.originalname.toLowerCase());
        
//         if (mimetype && extname) {
//             return cb(null, true);
//         } else {
//             cb(new Error('Invalid file type. Only JPEG, JPG, and PNG are allowed.'));
//         }
//     }
// });


// Importing route modules for different API endpoints
const categories = require('./routes/category');
const movies = require('./routes/movie');
const users = require('./routes/user');
const tokens = require('./routes/tokens');
const recommendations = require('./routes/recommend');

// Loading environment variables from the configuration file based on the environment
require('custom-env').env(process.env.NODE_ENV || 'local', './config');

// Connecting to MongoDB using the connection string from environment variables
mongoose.connect(process.env.CONNECTION_STRING);

// Initialize Express app
var app = express();

// Middleware setup
app.use(cors());
app.use(bodyParser.urlencoded({extended : true}));
app.use(express.json());

// Define API routes for various resources
app.use('/api/categories', categories);
app.use('/api/movies', movies);
app.use('/api/users', users);
app.use('/api/movies', recommendations);
app.use('/api/tokens', tokens);
app.use('/uploads', express.static('uploads'));

// Start the server and listen on the port specified in environment variables
app.listen(process.env.PORT);
