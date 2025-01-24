const express = require('express');
const router = express.Router();
const upload = require('../middleware/fileUpload.js');

// Endpoint for uploading an image
router.post('/image', upload.single('image'), (req, res) => {
  res.json({
    message: 'Image uploaded successfully',
    imageUrl: `/media/images/${req.file.filename}`,
  });
});

// Endpoint for uploading a video
router.post('/video', upload.single('video'), (req, res) => {
  res.json({
    message: 'Video uploaded successfully',
    videoUrl: `/media/movies/${req.file.filename}`,
  });
});

module.exports = router;
