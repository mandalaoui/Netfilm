#ifndef FUNC_FOR_TESTS_H
#define FUNC_FOR_TESTS_H

#include <string>
#include <fstream>

bool searchInFile(const std::string& value, const std::string& fileName);
void createOrClearFile(const std::string& fileName);
void insertToFile(const std::string& fileName, const std::string& content);
void duplicateFile(const std::string& sourceFile);
void compareFiles(const std::string& file1, const std::string& file2);

#endif // FUNCFORTESTS_H