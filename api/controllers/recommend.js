const recommendService = require('../services/recommend');

// Get recommended movies for a specific user and movie
const getRecommendedMovies = async (req, res) => {
    // Check if userId is provided and valid
    const userId = req.header('userId');
    if (!userId) {
        return res.status(400).json({ errors: ['Missing userId in header'] });
    }
    // Fetch the recommended movies based on userId and movieId from the request parameters
    const movies = await recommendService.getRecommendedMovies(userId, req.params.id);
    // If no movies are found, return a 404 response
    if (!movies) {
        return res.status(404).json({ errors: ['404 Not Found'] });
    }
    // Return the list of recommended movies as a JSON response
    res.json(movies);
}

// Add a movie to the user's watchlist
const addToWatchList = async (req, res) => {
    // Check if userId is provided and valid
    const userId = req.header('userId');
    if (!userId) {
        return res.status(400).json({ errors: ['Missing userId in header'] });
    }
    
    // Add the movie to the user's watchlist based on userId and movieId from the request parameters
    const movie = await recommendService.addToWatchList(userId, req.params.id);
    // If no movie is returned, return a 404 response
    if (!movie) {
        return res.status(404).json({ errors: ['404 Not Found'] });
    }
    // Return the movie added to the watchlist as a JSON response
    res.json(movie);
}

module.exports = { getRecommendedMovies , addToWatchList};