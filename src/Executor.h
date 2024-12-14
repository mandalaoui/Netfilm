
#ifndef EXECUTOR_H
#define EXECUTOR_H
#include "Runnable.h"

class Executor {
    public:
        virtual void execute(Runnable& command) = 0;
};
#endif





