#ifndef DELETE_COMMAND_H
#define DELETE_COMMAND_H
#include "ICommand.h"
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cctype>
#include "dataFuncs.h"


using namespace std;
class DeleteCommand : public ICommand {
    public:
        // Function that performs the action of adding user and movies to a user.
        string execute(string input) override;
        // Function to validate the input string.
        bool isInvalid(string input) override; 
    private:
        // Function that add a movie to a user's watchlist.
        void deleteMovie(string user, string movies);
        // Function that checks if the movies are in the user's watchlist.
        bool checkUserList(string user, string movies); 
};
#endif