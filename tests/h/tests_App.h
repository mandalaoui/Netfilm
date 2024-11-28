#ifndef TESTS_APP_H
#define TESTS_APP_H
#include <gtest/gtest.h>
#include "App.h"
#include "Help.h"
#include "Recommend.h"
#include "Add.h"

void createOrClearFile(const string& fileName);
void insertToFile(const string& fileName, const string& content);

TEST(AppRunTest, NoOutputForInvalidInput);
TEST(AppRunTest, NoChangeForInvalidInput);
TEST(AppRunTest, runCallsHelpOutput);
TEST(AppRunTest, runCallsHelpFiles);
TEST(AppRunTest, runCallsAddOutput);
TEST(AppRunTest, runCallsAddFiles);
TEST(AppRunTest, runCallsRecommendOutput);
TEST(AppRunTest, runCallsRecommendFiles);

int main(int argc, char **argv);

#endif // TESTS_APP_H