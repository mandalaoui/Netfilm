
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

using namespace std;

class AnExecutor : public Executor {
    private:
    public:
        void execute(Runnable& command) override;

};
#endif
