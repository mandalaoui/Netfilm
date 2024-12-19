#ifndef DATAFUNCS_H
#define DATAFUNCS_H
#include <string>
#include <iostream>
#include <sstream>
#include <fstream>
#include <vector> 

using namespace std;

    // Checks whether a specific string exists in the given file.
    bool isInFile(string str, string name_file);

    // Writes non-duplicate words from the string to the file.
    void writeToFile(string str, string name_file);

    // Reads data from the specified file and returns a vector of strings.
    vector<string> dataToVec(string name_file);

    void cleanFile(string name_file);

#endif // DATAFUNCS_H