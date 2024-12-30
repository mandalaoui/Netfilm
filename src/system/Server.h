#ifndef SERVER_H
#define SERVER_H

#include <iostream>
#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include "Executor.h"
#include "AnExecutor.h"
#include "ClientHandle.h"

using namespace std;

class Server
{
private:
    const int port;
    Executor* executor = new AnExecutor();
public:
    // Constructor to initialize the server with a specified port.
    Server(const int port);
    // set up and run the server.
    void runServer();

};
#endif
