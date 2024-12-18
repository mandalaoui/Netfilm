#ifndef MAPCOMMANDS_H
#define MAPCOMMANDS_H

#include <iostream>
#include <string>
#include <map>
#include "ICommand.h"   
#include "Post.h"
#include "Patch.h"
#include "Help.h"
#include "Recommend.h"
#include "DeleteCommand.h"

using namespace std;

// The MapCommands class is responsible for storing and managing commands.
class MapCommands {
    private:
        // Dynamically allocate memory for each command object, initializing them to their respective concrete classes.
        ICommand* post = new Post();
        ICommand* patch = new Patch();
//        ICommand* add = new Add();
        ICommand* get = new Recommend();
        ICommand* help = new Help();

        //object map commands.
        ICommand* deleteCommand = new DeleteCommand();

        map<string, ICommand*> commands;

     public:
         // Method to initialize the commands map.
        void createCommand();
        
        // Method to get the map of commands.
        map<string, ICommand*> getCommands();
        
        // Method to delete dynamically allocated command objects and free memory.
        void deleteCommands();  

};
#endif