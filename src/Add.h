#ifndef ADD_H
#define ADD_H
#include "ICommand.h"
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cctype>
#include "dataFuncs.h"

using namespace std;
class Add : public ICommand {
    public:
        // Function that performs the action of adding user and movies to a user.
        string execute(string input) override; 
        // Function to validate the input string.
        bool isInvalid(string input) override; 
};
#endif