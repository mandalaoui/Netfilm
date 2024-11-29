#ifndef ADD_H
#define ADD_H
#include "ICommand.h"
#include <iostream>
#include <string>
using namespace std;
class Add : public ICommand {
    public:
        void execute(string input) override;
        bool isInvalid(string input) override;
};
#endif