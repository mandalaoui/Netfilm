#ifndef RECOMMEND_H
#define RECOMMEND_H
#include "ICommand.h"
#include <iostream>
#include <string>
using namespace std;
class Recommend : public ICommand {
    public:
        void execute(string input) override;
};
#endif
