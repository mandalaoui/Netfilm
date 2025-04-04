
#ifndef ANEXECUTOR_H
#define ANEXECUTOR_H

#include <iostream>
#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include "MapCommands.h"
#include "Executor.h"
#include "ClientHandle.h"
#include <thread>
#include "ThreadPool.h"

using namespace std;

class AnExecutor : public Executor {
    public:
        AnExecutor(size_t numThreads);
        // Override the execute method of the Executor base class.
        void execute(Runnable& command) override;
    private:
        ThreadPool pool;

};
#endif
