#ifndef ADD_H
#define ADD_H
#include "ICommand.h"
#include <iostream>
class Add : public ICommand {
    public:
        void execute() override {
            std::cout << "You entered1: " << std::endl;
        }
        //void execute() override;
};
#endif