const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const movieValidation = require('../validation/movie');
const userValidation = require('../validation/user');
const recommendcontroller = require('../controllers/recommend');


//////////////////////////////////////////////////

const multer = require('multer');
const path = require('path');
// הגדרת המקום לשמור את הקבצים
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');  // המיקום שבו יאוחסנו הקבצים
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + path.extname(file.originalname));  // שינוי שם הקובץ
    }
});

// יצירת אובייקט multer (הוא ישמש לשמירה של קבצים)
const upload = multer({ storage: storage });

router.route('/')
    .post(upload.single('video'),userValidation.validateUserIdHeader, movieValidation.validateMovieInput, movieController.createMovie)


///////////////////////////////////////////////////

// Define routes for '/'
router.route('/')
    .get(userValidation.validateUserIdHeader, movieController.getMovies)
    //.post( userValidation.validateUserIdHeader, movieValidation.validateMovieInput, movieController.createMovie);

// Define routes for '/:id'
router.route('/:id')
    .get(userValidation.validateUserIdHeader, movieValidation.validateMovieId, movieController.getMovie)
    .put(userValidation.validateUserIdHeader, movieValidation.validateMovieId, movieValidation.validateMovieInput, recommendcontroller.deleteMovie, movieController.updateMovie)
    .delete(userValidation.validateUserIdHeader, movieValidation.validateMovieId, recommendcontroller.deleteMovie, movieController.deleteMovie);

// Define route for searching movies with a query.
router.route('/search/:query')
    .get(userValidation.validateUserIdHeader, movieController.getMovieIncludeQuery);

// Export the router to be used in the main application.
module.exports = router;