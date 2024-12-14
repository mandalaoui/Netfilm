
#include <iostream>
#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include "Runnable.h"
#include "ClientHandle.h"
#include "MapCommands.h"
#include "ICommand.h"
#define BUFFER_SIZE 1024

ClientHandle::ClientHandle(int clientSocket) : clientSocket(clientSocket) {}

void ClientHandle::run()
{
    MapCommands map;
    map.createCommand();
    std::map<std::string, ICommand*> commands = map.getCommands();

    char buffer[BUFFER_SIZE] = {0};

    while(true) {
        int bytesRead = read(this->clientSocket, buffer, BUFFER_SIZE);
        if(bytesRead <= 0) {
            close(this->clientSocket);
            break;
        }
        string clientMassage(buffer);

        string response;
        string task;
        size_t space = clientMassage.find(' ');
        task = clientMassage.substr(0, space);
        string inputForTask = clientMassage.substr(space + 1);

        try {
            // Check if the command exists in the map of commands, if exists and execute the command by calling its execute method.
            if (commands.find(task) != commands.end()) {
                response = commands[task]->execute(inputForTask);
            }
            send(this->clientSocket, response.c_str(), response.size(), 0);
            memset(buffer, 0, BUFFER_SIZE);
        }
        // Catch any exceptions thrown during execution and continue the loop.
        catch(...) {
            response = "400 Bad Request\n";
            send(this->clientSocket, response.c_str(), response.size(), 0);
            memset(buffer, 0, BUFFER_SIZE);
        }
    }

}
