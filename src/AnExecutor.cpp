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
    thread t(&Runnable::run, &command);    
    t.detach();
}
