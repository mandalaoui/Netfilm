#include "MapCommands.h"
#include "ICommand.h"
#include "Add.h"

void MapCommands::createCommand () {
    commands["add"] = add;
    //std::cout << "create commad: " << std::endl;

    /*ICommand* recommend = new Recommend();
    commands["recommend"] = recommend;

    ICommand* help = new Help();
    commands["help"] = help;*/
}

std::map<std::string, ICommand*> MapCommands:: getCommands() {
    //std::cout << "return commad: " << std::endl;
    return commands;
}

void MapCommands::deleteCommands() {
    delete add;
    //delete recommend;
    //delete help;
}
