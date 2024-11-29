#ifndef ICOMMAND_H
#define ICOMMAND_H
#include <string>
using namespace std;

// The ICommand interface defines the structure for command classes.
class ICommand {
    public:
        // Pure virtual function 'execute' that takes a string as input and will be overridden by concrete command classes.
        virtual void execute(string input) = 0;
};

#endif