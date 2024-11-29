#ifndef ADD_H
#define ADD_H
#include "ICommand.h"
#include <iostream>
#include <string>
#include <algorithm>
#include <sstream>
#include <fstream>
using namespace std;
class Add : public ICommand {
    public:
        void execute(string input) override;
        bool isInvalid(string input) override;
};
#endif