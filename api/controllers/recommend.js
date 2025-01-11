const recommendService = require('../services/recommend');
const userService = require('../services/user');


// Get recommended movies for a specific user and movie
const getRecommendedMovies = async (req, res, next) => {
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
    res.json(await movies);
    next();
}

// Add a movie to the user's watchlist
const addToWatchList = async (req, res, next) => {
    // Check if userId is provided and valid
    const userId = req.header('userId');
    if (!userId) {
        return res.status(400).json({ errors: ['Missing userId in header'] });
    }

    // Add the movie to the user's watchlist based on userId and movieId from the request parameters
    const watchList = await userService.addToWatchList(userId, req.params.id);
    
    // If no watchList is returned, return a 404 response
    if (!watchList) {
        return res.status(404).json({ errors: ['404 Not Found'] });
    }

    // Add the movie to the user's watchlist un the recommendation server
    const response = await recommendService.addToWatchList(userId, req.params.id);

    // If no movie is returned, return a 404 response
    if (!response) {
        return res.status(404).json({ errors: ['404 Not Found'] });
    }

    // Return the movie added to the watchlist as a JSON response
    res.json(watchList);
    next();
}

// Delete a movie from the user's watchlist
const deleteMovie = async (req, res, next) => {
    // Check if userId is provided and valid
    const userId = req.header('userId');
    if (!userId) {
        return res.status(400).json({ errors: ['Missing userId in header'] });
    }
    
    // Add the movie to the user's watchlist based on userId and movieId from the request parameters
    await recommendService.deleteMovie(req.params.id);
    next();
}

module.exports = { getRecommendedMovies , addToWatchList, deleteMovie};