#include "AnExecutor.h"
#include <iostream>
#include <string>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>
#include "MapCommands.h"
#include "ICommand.h"
#include <thread>

extern MapCommands map; 
using namespace std;

void AnExecutor::execute(Runnable& command)
{
    // Create a new thread to run the command's 'run' method. The thread is detached so it runs independently.
    //thread t(&Runnable::run, &command);    
    // Detach the thread, allowing it to run in the background without blocking the main execution flow.
    //t.detach();
     pool.submit([&command] { command.run(); });    
}
