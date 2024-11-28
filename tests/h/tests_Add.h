#ifndef TESTS_ADD_H
#define TESTS_ADD_H

#include <gtest/gtest.h>
#include "Add.h"
#include <iostream>
#include <fstream>

using namespace std;

// פונקציות עזר (declarations בלבד, ללא מימוש)
bool searchInFile(const string& value, const string& fileName);
void createOrClearFile(const string& fileName);
void insertToFile(const string& fileName, const string& content);
void duplicateFile(const string& sourceFile);
void compareFiles(const string& file1, const string& file2);

// טסטים (רק החתימות, ללא מימוש)
TEST(AddExecuteTest, UserIdNotFoundCreateNew);
TEST(AddExecuteTest, UserIdFoundAddingMovie);
TEST(AddExecuteTest, UserIdFoundMovieExists);
TEST(AddExecuteTest, UserIdFoundMovieRepeats);
TEST(AddExecuteTest, ValidInputWithSpaces);
TEST(AddExecuteTest, invalidInputs);

#endif // TESTS_ADD_H
