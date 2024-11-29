#ifndef ICOMMAND_H
#define ICOMMAND_H
#include <string>
using namespace std;
class ICommand {
    public:
        virtual void execute(string input) = 0;
};

#endif