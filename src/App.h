#ifndef APP_H
#define APP_H

#include <string>
#include <iostream>
#include <map>
#include "ICommand.h"
using namespace std;

// The App class is responsible for managing and executing commands.
class App {
    private:
        std::map<std::string, ICommand*> commands;
    public:
        // Constructor that initializes the 'commands' map
        App(std::map<std::string, ICommand*> commands);
        // Main function that runs the application
        void run();
};
#endif