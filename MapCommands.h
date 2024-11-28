#ifndef MAPCOMMANDS_H
#define MAPCOMMANDS_H

#include <iostream>
#include <string>
#include <map>
#include "ICommand.h"   
#include "Add.h"

using namespace std;

class MapCommands {
    public:
        ICommand* add = new Add();
        void createCommand();
        std::map<std::string, ICommand*> commands;
        std::map<std::string, ICommand*> getCommands();
        void deleteCommands();  

};
#endif