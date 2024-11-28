#ifndef TESTS_RECOMMEND_H
#define TESTS_RECOMMEND_H

#include <gtest/gtest.h>
#include "Recommend.h"
#include <iostream>
#include <fstream>

using namespace std;

// פונקציות עזר (declarations בלבד)
bool searchInFile(const string& value, const string& fileName);
void createOrClearFile(const string& fileName);
void insertToFile(const string& fileName, const string& content);
void duplicateFile(const string& sourceFile);
void compareFiles(const string& file1, const string& file2);

// טסטים (רק החתימות)
TEST(RecommendExecuteTest, UserIdNotFound);
TEST(RecommendExecuteTest, MovieIdNotFound);
TEST(RecommendExecuteTest, ExecuteReturnsCorrectRecommendation);
TEST(RecommendExecuteTest, NoMoreThanTenRecommendations);
TEST(RecommendExecuteTest, ExecuteDoesntChangeFiles);
TEST(RecommendExecuteTest, ValidInputWithSpaces);
TEST(RecommendExecuteTest, invalidInputs);

#endif // TESTS_RECOMMEND_H
