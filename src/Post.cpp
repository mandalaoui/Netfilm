#include "Post.h"
#include <iostream>
#include "ILocker.h"
#include "LockerThread.h"

using namespace std;
ILocker* postLock = new LockerThread();

// Overriding the execute function
string Post::execute(string input) {
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
    // Check if the user exists in the file, if exist return 404.
    if (isInFile(user, "users")) {
        return "404 Not Found";
    }
    else {
        // If the user doesn't exist, add the user and movies.
        Add add;
        add.execute(input);
    }
    return "201 Created";
}