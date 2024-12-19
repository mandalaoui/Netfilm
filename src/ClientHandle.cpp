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
#include <map>

#define BUFFER_SIZE 1024

using namespace std;

// Constructor for ClientHandle, initializing the client socket.
ClientHandle::ClientHandle(int clientSocket) : clientSocket(clientSocket) {}

// Implements the logic to handle client communication and execute commands.
void ClientHandle::run()
{
    // Initialize an object map and get the command map.
    MapCommands map;
    map.createCommand();
    std::map<std::string, ICommand*> commands = map.getCommands();
    char buffer[BUFFER_SIZE] = {0};

    // Infinite loop to handle client requests until the connection is closed.
    while(true) {
        // Read data from the client socket into the buffer. If no data is read or an error occurs, close the connection and exit the loop.
        int bytesRead = read(this->clientSocket, buffer, BUFFER_SIZE);
        if(bytesRead <= 0) {
            close(this->clientSocket);
            break;
        }
        // Convert the received buffer into a string.
        string clientMessage(buffer);

        // Read a line of clientMessage and take the first word in the string.
        string response;
        string task;
        size_t space = clientMessage.find(' ');
        if (space == string::npos) {
            task = clientMessage;
            inputForTask = "";
        } else {
            task = clientMessage.substr(0, space);
            string inputForTask = clientMessage.substr(space + 1);
        }
        try {
            // Check if the command exists in the map of commands, if exists and execute the command by calling its execute method.
            if (commands.find(task) == commands.end()) {
                throw invalid_argument("");
            }
            response = commands[task]->execute(inputForTask);

        }
        // Catch any exceptions thrown during execution and continue the loop.
        catch(...) {
            // Handle any exceptions during execution, send the response back to the client and clear the buffer for the next request.
            response = "400 Bad Request";
        }
        // Send the response back to the client and clear the buffer for the next request.
        send(this->clientSocket, response.c_str(), response.size(), 0);
        memset(buffer, 0, BUFFER_SIZE);

    }
}