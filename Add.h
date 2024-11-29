#ifndef ADD_H
#define ADD_H
#include "ICommand.h"
#include <iostream>
class Add : public ICommand {
    public:
        void execute() override;
};
#endif