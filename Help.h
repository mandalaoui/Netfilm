#ifndef HELP_H
#define HELP_H
#include "ICommand.h"
#include <iostream>
class Help : public ICommand {
    public:
        void execute(std::string word = "") override {
            std::cout << "You entered1: " << std::endl;
        }
        //void execute() override;
};
#endif