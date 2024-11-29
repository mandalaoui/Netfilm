#ifndef HELP_H
#define HELP_H
#include "ICommand.h"
#include <iostream>
class Help : public ICommand {
    public:
        void execute() override;
};
#endif