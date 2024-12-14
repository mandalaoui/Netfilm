
#ifndef CLIENTHANDLE_H
#define CLIENTHANDLE_H

#include <iostream>
#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include "Runnable.h"
#include "MapCommands.h"
#include "ICommand.h"

using namespace std;

class ClientHandle : public Runnable {
    private: 
        int clientSocket;
    public:
        void run() override;
        ClientHandle(int clientSocket);
};
#endif
