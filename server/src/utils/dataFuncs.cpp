#include <string>
#include <iostream>
#include <sstream>
#include <fstream>
#include "dataFuncs.h"
#include <vector>
#include "ILocker.h"
#include "LockerThread.h"

using namespace std;

map<string, ILocker*> lockers_map;

// Checks whether a specific string exists in the given file.
bool isInFile(string str, string name_file) {  
    // Open the file for reading from the specified path
    ifstream file("/usr/src/mytest/data/" + name_file + ".txt");
    if (!file.is_open()) {
        // Return false if the file could not be opened
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
            file.close();
            // The string is found in the file
            return true;
        }
    }
    file.close();
    // The string is not found
    return false;
}

// Writes non-duplicate words from the string to the file.
void writeToFile(string str, string name_file) {
    ILocker* locker = getLocker(name_file);
    locker->on(); 
    // Open the file for writing to the specified path
    ofstream file("/usr/src/mytest/data/" + name_file + ".txt", ios::app);
    if (!file.is_open()) {
        locker->off();
        // Exit the function if the file could not be opened
        return;
    }
    // Create a stringstream to split the input string into words
    stringstream ss(str);
    string word;
    while (ss >> word) {
        // Check if the word is not already in the file.
        if(!isInFile(word, name_file)) {
            // Write the word to the file if it does not exist
            file << word << endl;
        }
    }
    // Close the file after writing
    file.close();
    locker->off();
}

// Reads data from the specified file and returns a vector of strings.
vector<string> dataToVec(string name_file) {
    ILocker* locker = getLocker(name_file);
    locker->on(); 
    vector<string> vec;
    // Open the file for reading
    ifstream file("/usr/src/mytest/data/" + name_file + ".txt");
    if (!file.is_open()) {
        locker->off();
        // Return an empty vector if the file could not be opened
        return vec;
    }
    string str;
    // Read each line from the file and add it to the vector
    while (getline(file, str)) {
        // Insert the line into the vector
        vec.push_back(str);
    }
    file.close();
    locker->off();
    // Return the populated vector
    return vec;
}

// Clears the content of the file
void cleanFile(string name_file) {
    ILocker* locker = getLocker(name_file);
    locker->on(); 
    ofstream file("/usr/src/mytest/data/" + name_file + ".txt", ios::trunc);
    file.close();
    locker->off();
}

// Returns the locker for a given file
ILocker* getLocker(string name_file) {
    if (lockers_map.find(name_file) == lockers_map.end()) {
        lockers_map[name_file] = new LockerThread();
    }
    return lockers_map[name_file];
}