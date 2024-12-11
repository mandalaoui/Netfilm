#include <gtest/gtest.h>
#include <sstream>
#include "Delete.h"
#include "funcForTests.h"

using namespace std;


TEST(DeleteExecuteTest, deleteMovieFromUser) {
    Delete deleteMovie;
    const char* inputs[] = {"100","100 101", "100 102 103", "103 102 100 101", "103", "102 101"};
    const char* results[] = {"101\n102\n103", "102\n103", "101", "", "100\n101\n102", "100\n103"};

    setFile("users", "1");
    for (int i = 0; i < sizeof(inputs); i++)
    {
        setFile("1_watchlist", "100\n101\n102\n103");
        deleteMovie.execute("1" + inputs[i]);

        setFile("usersAfterDeleteMovie", results[i]);

        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("1_watchlist", "usersAfterDeleteMovie")) << "Comparison for " << string(inputs[i]) << " failed!";
    }
}

TEST(DeleteExecuteTest, invalidInputs) {
    Delete deleteMovie;
    const char* inputs[] = {"100 104","100 101 %", "100 102 103 104 105", "103 AB 102"};
    //const char* results[] = {"101\n102\n103", "102\n103", "101", "", "100\n101\n102", "100\n103"};

    setFile("users", "1");
    for (int i = 0; i < sizeof(inputs); i++)
    {
        setFile("1_watchlist", "100\n101\n102\n103");
        duplicateFile("1_watchlist", "usersAfterDeleteInvalid");
        deleteMovie.execute("1" + inputs[i]);

        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("1_watchlist", "usersAfterDeleteInvalid")) << "Comparison for " << string(inputs[i]) << " failed!";
    }
}

TEST(DeleteExecuteTest, userNotExist) {
    Delete deleteMovie;
    const char* inputs[] = {"5", "44"};

    setFile("users", "1\n2\n3\n4");
    for (int i = 0; i < sizeof(inputs); i++)
    {
        setFile(inputs[i] + "_watchlist", "100\n101\n102\n103");
        
        duplicateFile(inputs[i] + "_watchlist", "usersWithoutChange");
        deleteMovie.execute(inputs[i] + "101 102");

        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles(inputs[i] + "_watchlist", "usersWithoutChange")) << "Comparison for " << string(inputs[i]) << " failed!";
    }
}