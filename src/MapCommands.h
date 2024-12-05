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

// The MapCommands class is responsible for storing and managing commands.
class MapCommands {
    public:
        // Dynamically allocate memory for each command object, initializing them to their respective concrete classes.
        ICommand* add = new Add();
        ICommand* recommend = new Recommend();
        ICommand* help = new Help();

         // Method to initialize the commands map.
        void createCommand();
        
        //object map commands.
        std::map<std::string, ICommand*> commands;

        // Method to get the map of commands.
        std::map<std::string, ICommand*> getCommands();
        
        // Method to delete dynamically allocated command objects and free memory.
        void deleteCommands();  

};
#endif