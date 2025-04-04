#include "Add.h"
#include "ICommand.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <cctype>
#include <set>
#include "dataFuncs.h"

using namespace std;

// Function that performs the action of adding user and movies to a user.
string Add::execute(string input) {
    // Check if the input is valid.
    if (isInvalid(input)) {
        return "400 Bad Request";
    }
    size_t spacePos = input.find(' ');
    string user;
    string movies;
    if (spacePos != string::npos) {
        user = input.substr(0, spacePos); 
        movies = input.substr(spacePos + 1); 
    }   
    // Check if the user exists in the file, if exist check if the movies are already in the user's list.
    if (isInFile(user, "users")) {
        writeToFile(movies, user + "_watchlist");
    }
    else {
        // If the user doesn't exist, add the user and movies.
        writeToFile(user, "users");
        writeToFile(movies, user + "_watchlist");
    }
    // Close the users file after use.
    return "";
}

// Function to validate the input string.
bool Add::isInvalid(string input) {
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
    }
    if (wordCounter < 2) {
        return true;
    }
    // The input is valid.
    return false;
}