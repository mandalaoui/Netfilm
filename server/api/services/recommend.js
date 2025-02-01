const Movie = require('../models/movie');
const User = require('../models/user');
const net = require('net');
const mongoose = require('mongoose');

// Function to send a command to the recommendation server via a socket connection
const commandToServer = async (command) => {
    // Retrieve port and IP address from environment variables
    const port = process.env.RECCOMENDATION_PORT;
    const ip = process.env.RECCOMENDATION_IP;

    // Return a promise that resolves with the server response
    return new Promise((resolve, reject) => {
        // Create a new socket client
        const client = new net.Socket();

        // Connect the client to the server
        client.connect(port, ip, () => {
            client.write(command);
        });

        // Listen for data from the server
        client.on('data', (data) => {
            const response = data.toString();
            resolve(response);
            client.destroy();
        });

        // Listen for socket errors
        client.on('error', (err) => {
            // console.error(`Socket error: ${err.message}`);
            reject(err);
            client.destroy();
        });

        // Listen for the socket being closed
        client.on('close', () => {
        });
    });
};

// Function to get recommended movies from the server
const getRecommendedMovies = async (userId, movieId) => {
    try {        
        // Call the commandToServer function with the 'GET' command
        const serverResponse = await commandToServer(`GET ${userId} ${movieId}`);
        // Check if the response starts with "200 Ok\n\n"
        if (serverResponse.startsWith("200 Ok\n\n")) {
            // Extract the movie IDs from the response
            const movieIds = serverResponse
                .split("\n\n")[1] // Get the part after "200 Ok\n\n"
                .trim() // Remove extra spaces or newlines
                .split(" "); // Split movie IDs into an array
            const validMovieIds = movieIds.filter(id => mongoose.Types.ObjectId.isValid(id));
            if (validMovieIds.length === 0) return [];            // Fetch movie details from the database
            const movies = await Movie.find({ _id: { $in: movieIds } });
            return movies;
        }
        return serverResponse;
    } catch (err) {
        // console.error('Error in getting recommended movies:', err);
        throw err;
    }
}

// Function to add a movie to the watch list
const addToWatchList = async (userId, movieId) => {
    // Try to add the movie to the watch list with a 'POST' request
    let serverResponse = await commandToServer(`POST ${userId} ${movieId}`);
    // If the movie was successfully created, return the server response
    if (serverResponse === '201 Created') {
        return serverResponse;
    }
    // If the user already exists, try to update his movielist with a 'PATCH' request
    serverResponse = await commandToServer(`PATCH ${userId} ${movieId}`);
    return serverResponse;
}

// Function to delete a movie from user's watch list
const deleteMovieFromUser = async (userId, movieId) => {
    try {        
        // Call the commandToServer function with the 'GET' command
        return serverResponse = await commandToServer(`DELETE ${userId} ${movieId}`);
    } catch (err) {
        // console.error('Error in getting recommended movies:', err);
        throw err;
    }
}

// Function to delete a movie from system
const deleteMovie = async (movieId) => {
    const users = await User.find({});
    for (const user of users) {
        await deleteMovieFromUser(user._id, movieId);
    }
    return movieId;
}

module.exports = { getRecommendedMovies, addToWatchList, deleteMovie}