#ifndef DELETE_COMMAND_H
#define DELETE_COMMAND_H
#include "ICommand.h"
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cctype>

using namespace std;
class DeleteCommand : public ICommand {
    public:
        string execute(string input) override; // Function that performs the action of adding user and movies to a user.
        bool isInvalid(string input) override; // Function to validate the input string.
    private:
        void deleteMovie(string user, string movies); // Function that add a movie to a user's watchlist.
        bool isInFile(string str, ifstream& file); // Function to check if a string exists in a file
        bool checkUserList(string user, string movies); // Function that checks if the movies are in the user's watchlist
};
#endif