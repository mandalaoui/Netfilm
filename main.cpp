#include <iostream>
#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
#include <string>

int main() {
    MapCommands map;
    map.createCommand();
    std::map<std::string, ICommand*> commands = map.getCommands();
    App app(commands);
    app.run();
    map.deleteCommands();
    return 0;
}
