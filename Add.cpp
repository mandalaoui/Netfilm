#include "Add.h"
#include "ICommand.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <cctype>
using namespace std;

// Function that performs the action of adding user and movies to a user.
void Add::execute(string input) {
    // Check if the input is valid.
    if (!isInvalid(input)) {
        return;
    }
    // Open the users file for reading.
    ifstream users_file("/usr/src/mytest/data/users.txt");
    if (!users_file.is_open()) {
        // Try to create the file "users.txt" if it doesn't exist.
        ofstream create_file("/usr/src/mytest/data/users.txt");
        // If the file creation fails, display an error message.
        if (!create_file.is_open()) {
            cout << "Failed to create users.txt" << endl;
            return;
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
    if (isInFile(user, users_file)) {
        checkUserList(user, movies);
    }
    else {
        // If the user doesn't exist, add the user and movies.
        addUser(user, movies);
    }

    // Close the users file after use.
    users_file.close();
}

// Function to add a new user to the users file and his movie.
void Add::addUser(string user, string movies) {
    // Open the users file in append mode to add the new user.
    ofstream users_file("/usr/src/mytest/data/users.txt", std::ios::app); 
    if (!users_file.is_open()) {
        cerr << "opening faild" << endl;
        return;
    }
    // Write the username to the file and close the file.
    users_file << user << endl;
    users_file.close();
    // Convert the movies string into individual words (movies), iterate through each word (movie) and
    // add each movie to the user's watchlist.
    stringstream ss(movies);
    string word;
    while (ss >> word) {
        addMoviesToUser(user, word);
    }
}

// Function that checks if the user already has a each movie.
void Add::checkUserList(string user, string movies) {
    // Open the user's watchlist file
    ifstream user_watchlist("/usr/src/mytest/data/" + user + "_watchlist.txt");
     if (!user_watchlist.is_open()) {
        cerr << "opening faild" << endl;
        return;
    }
    // Convert the movie string into individual words (movies), iterate through each word (movie) and
    // check if the user had already each movie, add accordingly each movie to the user's watchlist.
    stringstream ss(movies);
    string word;
    while (ss >> word) {
        // Check if the movie is not already in the user's list.
        if(!isInFile(word, user_watchlist)) {
            // If the movie is not in the list, add it.
            addMoviesToUser(user, word);
        }
    }
    user_watchlist.close();
}

// Function that add a movie to a user's watchlist.
void Add::addMoviesToUser(string user, string movies) {
    // Open the user's watchlist file in append mode
    std::ofstream user_watchlist("/usr/src/mytest/data/" + user + "_watchlist.txt", std::ios::app);
    if (!user_watchlist.is_open()) {
        std::cerr << "opening faild" << std::endl;
        return;
    }
    // Write the movie to the file and close the file.
    user_watchlist << movies << endl;
    user_watchlist.close();
}   


// Function to check if a string exists in a file
bool Add::isInFile(string str, ifstream& file) {

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

// Function to validate the input string.
bool Add::isInvalid(string input) {
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



