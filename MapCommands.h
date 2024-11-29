#ifndef MAPCOMMANDS_H
#define MAPCOMMANDS_H

#include <iostream>
#include <string>
#include <map>
#include "ICommand.h"   
#include "Add.h"
#include "Help.h"
#include "Recommend.h"

using namespace std;

class MapCommands {
    public:
        ICommand* add = new Add();
        ICommand* recommend = new Recommend();
        ICommand* help = new Help();
        void createCommand();
        std::map<std::string, ICommand*> commands;
        std::map<std::string, ICommand*> getCommands();
        void deleteCommands();  

};
#endif