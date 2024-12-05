#ifndef HELP_H
#define HELP_H
#include "ICommand.h"
#include <iostream>
#include <string>
using namespace std;

// The Help class inherits from the ICommand interface.
class Help : public ICommand {
    public:
        //The execute method will take the user's input as a string and handle the "help" command logic.
        void execute(string input) override;
        //The invalid method checks whether the input string is valid or invalid.
        bool isInvalid(string input) override;
};
#endif