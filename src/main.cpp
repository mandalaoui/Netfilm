#include <iostream>
#include "App.h"
#include "MapCommands.h"
#include "ICommand.h"
#include "Server.h"
#include <string>
#include <cstdlib>

int main(int argc, char *argv[]) {
    int port = 12345; // ברירת מחדל אם לא הועבר ערך מהשורת פקודה

    // אם הועבר פורט כארגומנט
    if (argc > 1) {
        port = atoi(argv[1]); // המרת הערך לאינט
    }

    Server server(port);
    server.runServer();
    return 0;
}
