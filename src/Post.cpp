#include "Post.h"
#include <iostream>
#include "ILocker.h"
#include "LockerThread.h"

using namespace std;
ILocker* addLock = new LockerThread();

// Overriding the execute function
string Post::execute(string input) {
    // Check if the input is valid.
    if (!isInvalid(input)) {
        return "400 Bad Request";
    }
    postLock.on();
    // Open the users file for reading.
    ifstream users_file("/usr/src/mytest/data/users.txt");
    if (!users_file.is_open()) {
        // Try to create the file "users.txt" if it doesn't exist.
        ofstream create_file("/usr/src/mytest/data/users.txt");
        // If the file creation fails, display an error message.
        if (!create_file.is_open()) {
            postLock.off();
            return "400 Bad Request- Not open";
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
    // Check if the user exists in the file, if exist return 400.
    if (isInFile(user, users_file)) {
        postLock.off();
        users_file.close();
        return "400 Bad Request";
    }
    else {
        // If the user doesn't exist, add the user and movies.
        addUser(user, movies);
    }

    // Close the users file after use.
    users_file.close();
    postLock.off();

    return "201 Created";
}