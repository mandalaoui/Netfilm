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
        void addMoviesToUser(string user, string movies);
        void addUser(string user, string movies);
        bool isInFile(string str, fstream& file);

};
#endif