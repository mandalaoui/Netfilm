
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
#include <map>

using namespace std;

class ClientHandle : public Runnable {
    private: 
        int clientSocket;
    public:
        // Implements the logic to handle client communication and execute commands.
        void run() override;
        
        // Constructor for ClientHandle, initializing the client socket.
        ClientHandle(int clientSocket);
};
#endif
