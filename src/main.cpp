#include <iostream>
#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
#include "Server.h"
#include "Server.h"
#include <string>

int main() {
    // // Create an instance of the MapCommands class to manage the commands.
    // MapCommands map;
    // // Create an instance of the MapCommands class to manage the commands.
    // MapCommands map;

    // // Initialize the commands.
    // map.createCommand();
    // // Initialize the commands.
    // map.createCommand();

    // // Get the map of commands from MapCommands.
    // std::map<std::string, ICommand*> commands = map.getCommands();
    // // Get the map of commands from MapCommands.
    // std::map<std::string, ICommand*> commands = map.getCommands();

    // // Create an instance of the App class, passing the map of commands.
    // App app(commands);
    // // Create an instance of the App class, passing the map of commands.
    // App app(commands);

    // // Run the applicatio
    // app.run();
    // // Run the applicatio
    // app.run();

    // //Delete the dynamically allocated command objects to free memory.
    // map.deleteCommands();

    Server server(12345);
    server.runServer();
    return 0;
}
