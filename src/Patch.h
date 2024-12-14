#ifndef PATCH_H
#define PATCH_H
#include "Add.h"
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cctype>

using namespace std;
class Patch : public Add {
    public:
        string execute(string input) override; // Function that performs the action of adding user and movies to a user.
};
#endif