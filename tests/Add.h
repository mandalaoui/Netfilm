#ifndef ADD_H
#define ADD_H
#include "ICommand.h"
#include <iostream>
class Add : public ICommand {
    public:
        void execute(std::string word) override {
            std::cout << "You entered1: " + word << std::endl;
        }
        //void execute() override;
};
#endif