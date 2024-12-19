
#ifndef RUNNABLE_H
#define RUNNABLE_H
using namespace std;

class Runnable {
    public:
        // forces derived classes to implement the 'run' method.
        virtual void run() = 0;
};
#endif
