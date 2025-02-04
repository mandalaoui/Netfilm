const fs = require('fs');
const path = require('path');
const movieService = require('../services/movie');


const deleteFile = (filePath) => {
    fs.unlink(filePath, (err) => {
        if (err) {
            console.error(`Error deleting file ${filePath}:`, err);
        } else {
            console.log(`File deleted: ${filePath}`);
        }
    });
};

const deleteMovieFilesMiddleware = async (req, res, next) => {
    try {
        const movie = await movieService.getMovieById(req.params.id);
        if (!movie) {
            return res.status(404).json({ error: "Movie not found" });
        }
        const uploadPath = '/usr/src/app';

        const filesToDelete = ['image', 'video', 'trailer']
            .map(field => movie[field] ? path.join(uploadPath, movie[field]) : null)
            .filter(Boolean);

        await Promise.all(filesToDelete.map(deleteFile));

        next();
    } catch (error) {
        console.error("Error deleting movie files:", error);
        res.status(500).json({ error: "Failed to delete movie files" });
    }
};


module.exports = { deleteMovieFilesMiddleware };
