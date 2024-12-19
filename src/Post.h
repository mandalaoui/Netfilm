#ifndef POST_H
#define POST_H
#include "Add.h"
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <cctype>
#include "dataFuncs.h"

using namespace std;
class Post : public Add {
    public:
        // Function that performs the action of adding user and movies to a user.
        string execute(string input) override; 
};
#endif
