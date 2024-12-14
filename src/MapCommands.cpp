#include "MapCommands.h"
#include "ICommand.h"
#include "Add.h"
#include "Help.h"
#include "Recommend.h"
#include "DeleteCommand.h"

// Create and initialize the commands in the map.
void MapCommands::createCommand () {
    commands["POST"] = post;
    commands["PATCH"] = patch;
    //commands["add"] = add;
    commands["GET"] = recommend;
    commands["help"] = help;
    commands["DELETE"] = deleteCommand;
}

// Get the map of commands.
std::map<std::string, ICommand*> MapCommands:: getCommands() {
    return commands;
}

// Delete the command objects and free their memory.
void MapCommands::deleteCommands() {
    delete patch;
    delete post;
//    delete add;
    delete recommend;
    delete help;
    delete deleteCommand;
}
