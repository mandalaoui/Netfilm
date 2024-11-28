#include <string>
#include <iostream>
using namespace std;
class App: {
    public:
        void run() {
            string task;

            while(true) {
                cin >> user;
                size_t space = user.find(' ');
                string task = user.substr(0, space);
                cout << task << endl;
                try {
                    commands[task].execute(task);
                }
                catch() {}
            }
        }
}