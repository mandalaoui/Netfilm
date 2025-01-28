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
module.exports = upload;