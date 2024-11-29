#include <string>
#include <fstream>
#include "funcForTests.h"
#include <iostream>

using namespace std;


bool searchInFile(const string& value, const string& fileName)
{
    ifstream file(fileName);
    if (!file.is_open()) {
        // std::cerr << "file not available!" << std::endl;
        return false;
    }

    string line;
    while (getline(file, line)) {
        if (line.find(value) != string::npos) {
            // value found in the file
            file.close();
            return true;
        }
    }
    file.close();
    return false;
}

void createOrClearFile(const string& fileName) {
    ofstream file(fileName, ios::trunc); // delete mode
    if (file.is_open()) {
        //std::cout << "File created or cleared: " << fileName << std::endl;
    } else {
        cerr << "Failed to create or clear file: " << fileName << endl;
    }
}

void insertToFile(const string& fileName, const string& content) {
    ofstream file(fileName, ios::app); //adding mode
    if (file.is_open()) {
        file << content << endl;
        //std::cout << "Content added to file: " << fileName << std::endl;
    } else {
        cerr << "Failed to write to file: " << fileName << endl;
    }
}

void duplicateFile(const string& sourceFile) {
    ifstream src(sourceFile, ios::binary);
    createOrClearFile("duplicate");
    ofstream dest("duplicate", ios::binary);
    if (src.is_open() && dest.is_open()) {
        dest << src.rdbuf(); // copying data to new file
        //cout << "File duplicated: " << sourceFile << " -> " << destFile << std::endl;
    } else {
        cerr << "Failed to duplicate file: "; // << sourceFile << " -> " << dest << std::endl;
    }
}

void compareFiles(const string& file1, const string& file2) {
    ifstream f1(file1), f2(file2);
    if (!f1 || !f2) {
        std::cerr << "Failed to open one of the files!" << std::endl;
        return;
    }
    cout << "Comparing files:\n";
    string line1, line2;
    int lineNumber = 0;
    while (++lineNumber, std::getline(f1, line1) || std::getline(f2, line2)) {
        if (line1 != line2) {
            cout << "Difference at line " << lineNumber << ":\n"
                      << "File 1: " << line1 << "\n"
                      << "File 2: " << line2 << "\n";
        }
    }
    cout << "All done\n";
}
