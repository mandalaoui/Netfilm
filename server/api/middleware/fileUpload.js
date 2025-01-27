// const multer = require('multer');
// // const path = require("path");
// // const fs = require("fs");


// // Configure Multer storage
// const storage = multer.diskStorage({
//   destination: (req, file, cb) => {
//     // Save images in 'public/images' and videos in 'public/videos'
//     const folder = file.mimetype.startsWith("video") ? 'public/movies' : 'public/users';

//     // fs.mkdirSync(folder,{ recursive: true });

//     cb(null, folder);
//   },
//   filename: (req, file, cb) => {
//     // Save the file with its original name
//     cb(null, `${Date.now()}-${file.originalname}`);
//   },
// });

// const upload = multer({ storage });
///////////////////////////////////////////
const multer = require('multer');
const path = require('path');

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');  
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + path.extname(file.originalname));  
    }
});

const upload = multer({ storage: storage });


///////////////////////////////////
module.exports = upload;