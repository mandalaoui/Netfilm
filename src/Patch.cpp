#include "Patch.h"
#include <iostream>

using namespace std;

// Overriding the execute function
string Patch::execute(string input) {
        // Check if the input is valid.
    if (isInvalid(input)) {
        return "400 Bad Request";
    }
    // Open the users file for reading.
    ifstream users_file("/usr/src/mytest/data/users.txt");
    if (!users_file.is_open()) {
        // Try to create the file "users.txt" if it doesn't exist.
        ofstream create_file("/usr/src/mytest/data/users.txt");
        // If the file creation fails, display an error message.
        if (!create_file.is_open()) {
            return "400 Bad Request";
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
        // User doesn't exist
        users_file.close();
        return "404 Not Found";
    }

    // Close the users file after use.
    users_file.close();

    return "204 No Content";
}