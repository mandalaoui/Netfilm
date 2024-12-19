#include <iostream>
#include "MapCommands.h"
#include "ICommand.h"
#include "Server.h"
#include <string>
#include <cstdlib>

int main(int argc, char *argv[]) {
    // Default port value in case no port is provided as an argument.
    int port = 12345; 

    // If the program is run with a command-line argument, set the port to the provided value.
    if (argc > 1) {
        port = atoi(argv[1]); // Convert the argument (a string) to an integer.
    }

    // Create a Server object and pass the port number and start the server's run functionality.
    Server server(port);
    server.runServer();
    return 0;
}