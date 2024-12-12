#include <string>
#include <fstream>
#include "funcForTests.h"
#include "Server.h"
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
    ifstream src("/usr/src/mytest/data/"+sourceFile+".txt", ios::binary); // Open source file in binary mode for reading
    string fullNameFile = "/usr/src/mytest/data/"+newFileName+".txt";
    createOrClearFile(fullNameFile); // Create or clear the target file
    ofstream dest(fullNameFile, ios::binary); // Open target file in binary mode for writing
    if (src.is_open() && dest.is_open()) {
        dest << src.rdbuf(); // Copy all content from source to target file
    } else {
        cerr << "Failed to duplicate file: "; // << sourceFile << " -> " << dest << std::endl;
    }

    printFileContents(sourceFile);
    printFileContents(newFileName);
}

// Function to compare the content of two files line by line
bool compareFiles(const string& file1, const string& file2) {
    ifstream f1("/usr/src/mytest/data/"+file1+".txt"), f2("/usr/src/mytest/data/"+file2+".txt"); // Open both files for reading
    if (!f1) { // Check if either file failed to open
            if (!f2) {
                return true;
            }
        std::cerr << "Failed to open one of the files!" << std::endl;
        return false;
    }
    if (!f2) { // Check if either file failed to open
        std::cerr << "Failed to open one of the files!" << std::endl;
        return false;
    }
    //cout << "Comparing files: " << file1 << " & " << file2 << "\n"; // Log the comparison process
    string line1, line2;
    int lineNumber = 0; // Line counter
    // Read lines from both files
    int isSame = 1; // the same
    while (++lineNumber, std::getline(f1, line1) && std::getline(f2, line2)) {
        if (line1 != line2) { 
            isSame = 0; // not the same
        }
    }
    if (isSame == 0) {
        cout << "Files are not identical!" << endl;
        cout << file1 << ": \n";
        printFileContents(file1);
        cout << file2 << ": \n";
        printFileContents(file2);
        return false; // Signal failure
    }
    //cout << "Files are identical\n";
    return true; // Signal success

}

void printFileContents(const std::string& filename) {
    string fullNameFile = "/usr/src/mytest/data/"+filename+".txt";
    std::ifstream file(fullNameFile); // Open the file for reading
    if (!file) { // Check if the file opened successfully
        std::cerr << "Error: Could not open file \"" << fullNameFile << "\"!" << std::endl;
        return;
    }

    std::string line;
    while (std::getline(file, line)) { // Read the file line by line
        std::cout << line << std::endl; // Print each line to the console
    }

    file.close(); // Close the file
}

// Function to reset a file with new content
void setFile(const string& fileName, const string& content) {
    string fullNameFile = "/usr/src/mytest/data/"+fileName+".txt";
    createOrClearFile(fullNameFile);
    insertToFile(fullNameFile,content);
}

// Function to check the correction of server's response
bool checkResponseFromServer(const string& clientMessage, const string& expectedResponse) {
    // Step 1: Start the server
    Server server;
    if (!server.start(8080)) {
        return false; // If the server failed to start, return false
    }

    // Step 2: Simulate the server receiving the message
    server.receiveMessage(clientMessage); // A function that simulates receiving a message
    
    // Step 3: Capture the server's response directly from the server
    std::string actualResponse = server.getLastResponse();
    
    // Step 4: Compare response to expected result
    bool isEqual = (actualResponse == expectedResponse);
    
    // Step 5: Clean up
    server.stop();

    return isEqual;
}
