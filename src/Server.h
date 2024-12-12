#ifndef SERVER_H
#define SERVER_H

#include <string>
#include <iostream>

using namespace std;

// The App class is responsible for managing and executing commands.
class Server {
    public:
    
    bool start(int port);
    void receiveMessage(const string& clientMessage); 
    string getLastResponse();
    void stop();      
};
#endif