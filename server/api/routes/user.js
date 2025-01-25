const express = require('express');
var router = express.Router();
const userController = require('../controllers/user');
const userValidation = require('../validation/user')
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
    .post(upload.single('photo'),userValidation.validateUserInput, userController.createUser)


///////////////////////////////////////////////////

// Route for creating a new user. Validates input before calling the controller's createUser function.
// router.route('/')
//     .post(userValidation.validateUserInput, userController.createUser);

// Route for retrieving a user by their ID. Calls the controller's getUser function.
router.route('/:id')
    .get(userValidation.validateUserIdHeader, userValidation.validateUserId, userController.getUser)

// Exports the router to be used in the application.
module.exports = router;