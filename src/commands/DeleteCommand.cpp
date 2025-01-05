#include "DeleteCommand.h"
#include "ICommand.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <cctype>
#include <set>
#include <queue>
#include <vector>
#include <string>
#include "dataFuncs.h"

using namespace std;

// Function that performs the action of adding user and movies to a user.
string DeleteCommand::execute(string input) {
    // Check if the input is valid.
    if (isInvalid(input)) {
        return "400 Bad Request";
    }
    // Split the input into two parts: username and movies.
    size_t spacePos = input.find(' ');
    string user;
    string movies;
    if (spacePos != string::npos) {
        user = input.substr(0, spacePos); 
        movies = input.substr(spacePos + 1); 
    }   
    // Check if the user exists in the file, if exist check if the movies are already in the user's list.
    if (!isInFile(user, "users") || !checkUserList(user, movies)) {
        // User or movie doesn't exist
        return "404 Not Found";
    }
    return "204 No Content";
}

// Function to validate the input string.
bool DeleteCommand::isInvalid(string input) {
    // If the input is less than 3 characters long, it's invalid.
    if (input.size() < 2) {
        return true;
    }
    // Convert the input string into a stringstream.
    stringstream ss(input);
    string word;
    int wordCounter = 0;
    // For each word in the input.
    while (ss >> word) {    
        wordCounter++;
        // Attempt to convert the string word to an unsigned long.
        // try {
        //     unsigned long num1 = stoul(word);
        // } catch (const invalid_argument& e) {
        //     // If conversion fails, the input is invalid.
        //     return true;
        // } catch (const out_of_range& e) {
        //     // If the numbers are too large, the input is invalid.
        //     return true;
        // }

    }
    if (wordCounter < 2) {
        return true;
    }
    //The input is valid.
    return false;
}

// Function that add a movie to a user's watchlist.
void DeleteCommand::deleteMovie(string user, string movie) {
    // Queue to store IDs that will not be deleted
    queue<string> movieQueue;
    string currentMovieId;
    vector<string> user_watchlist = dataToVec(user + "_watchlist");
    cleanFile(user + "_watchlist");

    for (int i = 0; i < user_watchlist.size(); i++) {
        if (user_watchlist[i] != movie) {
            movieQueue.push(user_watchlist[i]);
        }
    }
    // Write the IDs back into the file
    while (!movieQueue.empty()) {
        writeToFile(movieQueue.front(), user + "_watchlist");
        movieQueue.pop();
    }
}

// Function that checks if the user already has a each movie.
bool DeleteCommand::checkUserList(string user, string movies) {
    set<string> deletedMovies;
    stringstream ss(movies);
    string word;
    
    // First, check if all movies are in the user's watchlist.
    while (ss >> word) {
        if (!isInFile(word, user + "_watchlist")) {
            // If any movie is not in the watchlist, close the file and return false.
            return false;
        }
    }

    ss.clear();  // Reset the stringstream for reuse
    ss.str(movies);  // Set the stringstream's content to the input again

    while (ss >> word) {      
        // Check if the movie is not already in the set.
        if (isInFile(word, user + "_watchlist")) {
            deleteMovie(user, word);
        }
    }
    return true;
}
