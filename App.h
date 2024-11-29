#ifndef APP_H
#define APP_H

#include <string>
#include <iostream>
#include <map>
#include "ICommand.h"
using namespace std;

class App {
    private:
        std::map<std::string, ICommand*> commands;
    public:
        App(std::map<std::string, ICommand*> commands);
        void run();

};
#endif