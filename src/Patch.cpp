#include "Patch.h"
#include <iostream>
#include <vector>
#include "dataFuncs.h"

using namespace std;

// Overriding the execute function
string Patch::execute(string input) {
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
    if (isInFile(user, "users")) {
        Add add;
        add.execute(input);
    }
    else {
        return "404 Not Found";
    }
    return "204 No Content";
}