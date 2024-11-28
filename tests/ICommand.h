#ifndef ICOMMAND_H
#define ICOMMAND_H
#include <iostream>

class ICommand {
    public:
        virtual void execute(std::string word) {
            std::cout << "You entered: 456 " + word << std::endl;
        }
};

#endif