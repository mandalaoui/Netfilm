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
    ifstream users_file("/usr/src/mytest/data/users.txt");
    if (!users_file.is_open()) {
        cout << "opening faild1" << endl;
        return;
    }

    size_t spacePos = input.find(' ');
    string user;
    string movies;
    if (spacePos != string::npos) {
        user = input.substr(0, spacePos); 
        movies = input.substr(spacePos + 1); 
    }
    
    if (isInFile(user, users_file)) {
        addMoviesToUser(user, movies);
    }
    else {
        addUser(user, movies);
    }

    users_file.close();
}
void Add::addUser(string user, string movies) {
    fstream outputFile("/usr/src/mytest/data/users.txt", std::ios::in | std::ios::out);
    if (!outputFile) {
        cerr << "opening faild" << endl;
        return;
    }
    outputFile.seekp(0, std::ios::end); 
    outputFile << user << endl;
    outputFile.close();
    ofstream file_movie("/usr/src/mytest/data/" + user + "_watchlist.txt");
    if (!file_movie) {
        std::cerr << "opening faild" << std::endl;
        return;
    }
    file_movie.close();
    addMoviesToUser(user, movies);
}


void Add::addMoviesToUser(string user, string movies) {
    fstream user_watchlist("/usr/src/mytest/data/" + user + "_watchlist.txt", std::ios::in | std::ios::out);
     if (!user_watchlist) {
        cerr << "opening faild" << endl;
        return;
    }
    stringstream ss(movies);
    string word;
    while (ss >> word) {
        if(!isInFile(word, user_watchlist)){
            user_watchlist.seekp(0, std::ios::end); 
            user_watchlist << word << endl;
        }
    }
    user_watchlist.close();
}   

/*bool Add::isInFile(string str, fstream& file) {
    if (!file.is_open()) {
        cout << "Error: File not open!" << endl;
        return false;
    }
    // Reset any error flags that may have occurred during file operations
    file.clear();
    // Move the read pointer to the beginning of the file
    file.seekg(0, ios::beg);

    string word;
    // Read the file line by line to check for the specified string
    while (getline(file, word)) {
        if (word == str){
            // The string is found in the file
            return true;
        }
    }
    // The string is not found
    return false;
}*/

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



