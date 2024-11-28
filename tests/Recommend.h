#ifndef RECOMMEND_H
#define RECOMMEND_H
#include "ICommand.h"
#include <iostream>
class Recommend : public ICommand {
    public:
        void execute() override {
            std::cout << "You entered1: " << std::endl;
        }
        //void execute() override;
};
#endif