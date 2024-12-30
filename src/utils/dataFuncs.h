#ifndef DATAFUNCS_H
#define DATAFUNCS_H
#include <string>
#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <map>
#include "ILocker.h"
#include "LockerThread.h"

using namespace std;

    // Declare an external map that associates file names with their corresponding lock objects.
    extern map<string, ILocker*> lockers_map;

    // Checks whether a specific string exists in the given file.
    bool isInFile(string str, string name_file);

    // Writes non-duplicate words from the string to the file.
    void writeToFile(string str, string name_file);

    // Reads data from the specified file and returns a vector of strings.
    vector<string> dataToVec(string name_file);

    // Clears the content of the file
    void cleanFile(string name_file);

    // Returns the locker for a given file
    ILocker* getLocker(string name_file);

#endif // DATAFUNCS_H