#include "Server.h"
#include <iostream>
#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h> 
#include <arpa/inet.h>
#include "ClientHandle.h"
#include "AnExecutor.h"

using namespace std;

// Constructor to initialize the server with a specified port.
Server::Server(const int port) : port(port) {}
    
// set up and run the server.
void Server::runServer()
{
    int serverSocket, clientSocket;
    struct sockaddr_in serverAddr, clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);

    //create a socket.
    if((serverSocket = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    //configure server address
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY;    
    serverAddr.sin_port = htons(port);

    //Bind The Socket
    if (bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) < 0) {
        perror("Bind failed");
        exit(EXIT_FAILURE);
    }

    //Listen
    if(listen(serverSocket,0) < 0) {
        perror("Listen failed");
        exit(EXIT_FAILURE);
    }

    cout << "server is listening to Port - " << port << endl;
    // Main loop to accept and handle client requests.
    while (true) {
        // Accept an incoming client connection.
        if ((clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr, &clientAddrLen)) < 0) {
            perror("Accept failed");
            continue;
        }
        // Create a ClientHandle object to handle the client's requests.
        Runnable* clientHandle = new ClientHandle(clientSocket);
        // Pass the ClientHandle to the executor for execution in a separate thread.
        executor->execute(*clientHandle);
    }
}
