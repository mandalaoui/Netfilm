#ifndef FUNC_FOR_TESTS_H
#define FUNC_FOR_TESTS_H
#include <string>
#include <fstream>

using namespace std;

// Function declarations:

// Creates a new file or clears an existing one by overwriting it with an empty content.
void createOrClearFile(const string& fileName);

// Inserts a given string as a new line into the specified file.
void insertToFile(const string& fileName, const string& content);

// Creates a duplicate of an existing file by copying all its content into a new file.
void duplicateFile(const string& sourceFile, const string& newFileName);

// Compares the content of two files line by line.
bool compareFiles(const string& file1, const string& file2);

// Creates a file with the given name and sets its content to the specified string.
void setFile(const string& fileName, const string& content);

void printFileContents(const string& filename);

#endif // FUNCFORTESTS_H