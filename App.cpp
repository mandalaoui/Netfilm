#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
using namespace std;

App::App(std::map<std::string, ICommand*> commands) : commands(commands) {}

void App::run() {
    string task;
    string userInput;

    while(true) {
        getline(cin, userInput);
        size_t space = userInput.find(' ');
        task = userInput.substr(0, space);
        cout << task << endl;
        try {
            cout << "3" << endl;
            commands[task]->execute();
            cout << "2" << endl;

        }
        catch(...) {
            cout << "1" << endl;
        }
    }
}
