#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
#include <map>
using namespace std;
// Constructor for the App class, which accepts a map of commands.
App::App(std::map<std::string, ICommand*> commands) : commands(commands) {}

// Main function that starts the loop and waits for user input to run commands.
void App::run() {
    string task;
    string userInput;
    string inputForTask;
    
    // Infinite loop to keep running the application.
    while(true) {
        // Read a line of input from the user and take the first word in the string.
        getline(cin, userInput);
        size_t space = userInput.find(' ');
        task = userInput.substr(0, space);
        inputForTask = userInput.substr(space + 1);

        try {
            // Check if the command exists in the map of commands, if exists and execute the command by calling its execute method.
            if (commands.find(task) != commands.end()) {
                cout << commands[task]->execute(inputForTask) << endl;
            }
        }
        // Catch any exceptions thrown during execution and continue the loop.
        catch(...) {
            continue;
        }
    }
}
