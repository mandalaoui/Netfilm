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

AnExecutor::AnExecutor(size_t numThreads) : pool(numThreads) {}

// Executes the given Runnable command.
// The command is submitted to the thread pool for execution.
// The Runnable's run() method is invoked when the thread processes the task.
void AnExecutor::execute(Runnable& command)
{
     pool.submit([&command] { command.run(); });    
}
