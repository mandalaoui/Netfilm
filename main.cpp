#include <iostream>
#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
#include <string>

int main() {
    /*std::string str; 
    std::cout << "You entered: " << str << std::endl;
    std::cin >> str;  
    std::cout << "You entered: " << str << std::endl;*/
    MapCommands map;
    map.createCommand();
    std::map<std::string, ICommand*> commands = map.getCommands();
    App app(commands);
    app.run();
    map.deleteCommands();
    return 0;
}
