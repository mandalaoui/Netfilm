#include "Add.h"
#include "string"
#include "ICommand.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <cctype>
using namespace std;
void Add::execute(string input) {
    if (!isInvalid(input)) {
        return;
    }
    /*ifstream file(users.txt);
    string line;
    while (getline(file, line))
    {
        if (line == input)
        {
        }
        
    }
    file.close();*/


}
bool Add::isInvalid(string input) {
    if (input.size() < 3) {
        return false;
    }

    stringstream ss(input);
    string word;
    int count = 0;

    while (ss >> word) {
        if (count == 0)
        {
            count++;
            continue;
        }
        bool isNumber = true;
        for (char ch : word) {
            if (!isdigit(ch)) {
                return false;
            }
        }
        count++;
    }
    return true;
}



