#include <iostream>
#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
#include <string>

int main() {
    // Create an instance of the MapCommands class to manage the commands.
    MapCommands map;

    // Initialize the commands.
    map.createCommand();

    // Get the map of commands from MapCommands.
    std::map<std::string, ICommand*> commands = map.getCommands();

    // Create an instance of the App class, passing the map of commands.
    App app(commands);

    // Run the applicatio
    app.run();

    //Delete the dynamically allocated command objects to free memory.
    map.deleteCommands();
    return 0;
}
