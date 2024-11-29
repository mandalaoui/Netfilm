#include <string>
#include <fstream>
#include "funcForTests.h"
#include <iostream>

using namespace std;

// Function to create a new file or clear the content of an existing file
void createOrClearFile(const string& fileName) {
    ofstream file(fileName, ios::trunc); // delete existing content
    if (!file.is_open()) {
        cerr << "Failed to create or clear file: " << fileName << endl;
    }
}

// Function to append content to a file
void insertToFile(const string& fileName, const string& content) {
    ofstream file(fileName, ios::app); // Open file in append mode
    if (file.is_open()) {
        file << content << endl; // Write content to the file
    } else {
        cerr << "Failed to write to file: " << fileName << endl;
    }
}

// Function to duplicate the content of one file into another
void duplicateFile(const string& sourceFile, const string& newFileName) {
    ifstream src(sourceFile, ios::binary); // Open source file in binary mode for reading
    createOrClearFile(newFileName); // Create or clear the target file
    ofstream dest(newFileName, ios::binary); // Open target file in binary mode for writing
    if (src.is_open() && dest.is_open()) {
        dest << src.rdbuf(); // Copy all content from source to target file
    } else {
        cerr << "Failed to duplicate file: "; // << sourceFile << " -> " << dest << std::endl;
    }
}

// Function to compare the content of two files line by line
void compareFiles(const string& file1, const string& file2) {
    ifstream f1(file1), f2(file2); // Open both files for reading
    if (!f1 || !f2) { // Check if either file failed to open
        std::cerr << "Failed to open one of the files!" << std::endl;
        return;
    }
    cout << "Comparing files:\n"; // Log the comparison process
    string line1, line2;
    int lineNumber = 0; // Line counter
    // Read lines from both files
    while (++lineNumber, std::getline(f1, line1) || std::getline(f2, line2)) {
        if (line1 != line2) { // Compare the lines
            cout << "Difference at line " << lineNumber << ":\n"
                      << "File 1: " << line1 << "\n"
                      << "File 2: " << line2 << "\n";
        }
    }
    cout << "All done\n";
}

// Function to reset a file with new content
void setFile(const string& fileName, const string& content) {
    createOrClearFile(fileName);
    insertToFile(fileName,content);
}