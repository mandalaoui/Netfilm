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
#define MAX_CLIENT 100

using namespace std;

class Server
{
private:
    const int port;
    Executor* executor = new AnExecutor(MAX_CLIENT);
public:
    // Constructor to initialize the server with a specified port.
    Server(const int port);
    // set up and run the server.
    void runServer();

};
#endif
