#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
#include <map>
using namespace std;

App::App(std::map<std::string, ICommand*> commands) : commands(commands) {}

void App::run() {
    string task;
    string userInput;

    while(true) {
        getline(cin, userInput);
        size_t space = userInput.find(' ');
        task = userInput.substr(0, space);
        try {
            if (commands.find(task) != commands.end()) {
                commands[task]->execute(userInput);
            }
        }
        catch(...) {
            continue;
        }
    }
}
