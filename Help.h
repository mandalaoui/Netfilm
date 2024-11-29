#ifndef HELP_H
#define HELP_H
#include "ICommand.h"
#include <iostream>
#include <string>
using namespace std;
class Help : public ICommand {
    public:
        void execute(string input) override;
};
#endif