#include "Help.h"
#include "string"
#include "ICommand.h"
#include <iostream>
#include <string>
#include <sstream>
using namespace std;

// This function will takes the input string from the user and show the right format.
void Help::execute(string input) {
    // Check if the input is invalid using the isInvalid function.
    if (isInvalid(input))
    {
        cout << "add [userid] [movieid1] [movieid2] …" << endl;
        cout << "recommend [userid] [movieid]" << endl;
        cout << "help" << endl;
    }
}

// This function checks if the input string is invalid.
bool Help::isInvalid(string input) {
    istringstream stream(input);
    string word;
    int count = 0;

    // Loop through the input string and count the words.
    while (stream >> word)
    {
        count++;
    }
    
    // If there is only one word in the input, we consider it invalid.
    if (count == 1) {
        return true;
    }
    return false;
}
