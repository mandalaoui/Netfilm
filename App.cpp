#include "App.h"
App::App(std::map<std::string, ICommand*> commands) : commands(std::move(commands)) {
    // המימוש של הקונסטרוקטור, את מחלקת ה-commands מגדירים כאן
}
void App::run() {
    std::cout << "App is running" << std::endl;

}