#ifndef PATCH_H
#define PATCH_H
#include "ICommand.h"
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cctype>

using namespace std;
class Patch : public ICommand {
    public:
        void execute(string input) override; // Function that performs the action of adding user and movies to a user.
        bool isInvalid(string input) override; // Function to validate the input string.
        void addMoviesToUser(string user, string movies); // Function that add a movie to a user's watchlist.
        void addUser(string user, string movies); // Function to add a new user to the users file and his movie.
        bool isInFile(string str, ifstream& file); // Function to check if a string exists in a file
        void checkUserList(string user, string movies); // Function that checks if the user already has a each movie.
};
#endif