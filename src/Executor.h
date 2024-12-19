
#ifndef EXECUTOR_H
#define EXECUTOR_H
#include "Runnable.h"

// Abstract interface for executing tasks represented by Runnable objects.
class Executor {
    public:
        // Pure virtual function to execute a Runnable command
        virtual void execute(Runnable& command) = 0;
};
#endif





