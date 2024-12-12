#include "MapCommands.h"
#include "ICommand.h"
#include "Add.h"
#include "Help.h"
#include "Get.h"

// Create and initialize the commands in the map.
void MapCommands::createCommand () {
    commands["add"] = add;
    commands["GET"] = get;
    commands["help"] = help;
}

// Get the map of commands.
std::map<std::string, ICommand*> MapCommands:: getCommands() {
    return commands;
}

// Delete the command objects and free their memory.
void MapCommands::deleteCommands() {
    delete add;
    delete get;
    delete help;
}
