#include "MapCommands.h"
#include "ICommand.h"
#include "Add.h"
#include "Help.h"
#include "Recommend.h"

void MapCommands::createCommand () {
    commands["add"] = add;
    commands["recommend"] = recommend;
    commands["help"] = help;
}

std::map<std::string, ICommand*> MapCommands:: getCommands() {
    return commands;
}

void MapCommands::deleteCommands() {
    delete add;
    delete recommend;
    delete help;
}
