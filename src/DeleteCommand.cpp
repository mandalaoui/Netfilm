#include "DeleteCommand.h"
#include "ICommand.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <cctype>
#include <set>
#include <queue>
using namespace std;

// Function that performs the action of adding user and movies to a user.
string DeleteCommand::execute(string input) {
    // Check if the input is valid.
    if (!isInvalid(input)) {
        return "400 Bad Request";
    }
    // Open the users file for reading.
    ifstream users_file("/usr/src/mytest/data/users.txt");
    if (!users_file.is_open()) {
        // display an error message.
        if (!users_file.is_open()) {
            return "404 Not Found";
        }
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
    if (!isInFile(user, users_file) || !checkUserList(user, movies)) {
        // User or movie doesn't exist
        return "404 Not Found";
    }

    // Close the users file after use.
    users_file.close();

    return "204 No Content";
}

// Function to validate the input string.
bool DeleteCommand::isInvalid(string input) {
    // If the input is less than 3 characters long, it's invalid.
    if (input.size() < 2) {
        return false;
    }
    // Convert the input string into a stringstream.
    stringstream ss(input);
    string word;

    // For each word in the input.
    while (ss >> word) {    
        // Check if every character in the word is a digit.
        for (char ch : word) {
            //If any character is not a digit, return false.
            if (!isdigit(ch)) {
                return false;   
            }
        }

        // Attempt to convert the string `word` to an unsigned long.
        try {
            unsigned long num1 = stoul(word);
        } catch (const invalid_argument& e) {
            // If conversion fails, the input is invalid.
            return false;
        } catch (const out_of_range& e) {
            // If the numbers are too large, the input is invalid.
            return false;
        }
    }
    //The input is valid.
    return true;
}

// Function that add a movie to a user's watchlist.
void DeleteCommand::deleteMovie(string user, string movie) {
    // Queue to store IDs that will not be deleted
    queue<int> movieQueue;
    int currentMovieId;

    // Read from the file and add IDs to the queue, except the one to be deleted
    
    // Open the user's watchlist file in append mode
    std::ifstream user_watchlist("/usr/src/mytest/data/" + user + "_watchlist.txt", std::ios::app);
    if (!user_watchlist.is_open()) {
        return;
    }
    while (user_watchlist >> currentMovieId) {
        if (currentMovieId != stoi(movie)) {
            movieQueue.push(currentMovieId);
        }
    }
    user_watchlist.close();

    std::ofstream outputFile("/usr/src/mytest/data/" + user + "_watchlist.txt", std::ios::trunc);
    if (!outputFile.is_open()) {
        return;
    }

    // Write the IDs back into the file
    while (!movieQueue.empty()) {
        outputFile << movieQueue.front() << endl;
        movieQueue.pop();
    }

    outputFile.close();
}

// Function to check if a string exists in a file
bool DeleteCommand::isInFile(string str, ifstream& file) {
    // Move the file pointer to the beginning to reset any previous error flags.
    file.seekg(0, ios::beg);
    string word;
    // Read the file line by line to check for the specified string.
    while (getline(file, word)) {
        if (word == str) {
            // The string is found in the file.
            return true;
        }
    }
    // The string is not found
    return false;
}

// Function that checks if the user already has a each movie.
bool DeleteCommand::checkUserList(string user, string movies) {
    // Open the user's watchlist file
    ifstream user_watchlist("/usr/src/mytest/data/" + user + "_watchlist.txt");
    if (!user_watchlist.is_open()) {
        return false;
    }
    //create set that include all the movies the users ask to add.
    set<string> deletedMovies;
    stringstream ss(movies);
    string word;
    
    // First, check if all movies are in the user's watchlist.
    while (ss >> word) {
        if (!isInFile(word, user_watchlist)) {
            // If any movie is not in the watchlist, close the file and return false.
            user_watchlist.close();
            return false;
        }
    }

    ss.clear();  // Reset the stringstream for reuse
    ss.str(movies);  // Set the stringstream's content to the input again

    while (ss >> word) {      
        // Check if the movie is not already in the set.
        if (deletedMovies.find(word) == deletedMovies.end()) {
            // If the movie is not in the list, delete it.
            deleteMovie(user, word);
            // Add the movie to the set to prevent further duplicates
            deletedMovies.insert(word);
        }
    }
    user_watchlist.close();
    return true;
}