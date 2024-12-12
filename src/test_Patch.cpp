#include <gtest/gtest.h>
#include <iostream>
#include <fstream>
#include "Patch.h"
#include "funcForTests.h"

using namespace std;
// Tests for function "Execute" in class "Patch"

    // Test for adding a new user
TEST(PatchExecuteTest, UserIdNotFound) {
    // Initialize the "users" file (clear or create it)   
    const string inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs); ++i) {
        setFile("users","12\n15\n20\n19");
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("PATCH "+ inputs[i] + " 35 20", "404 Not Found"));
        // Create a file to store the result as after patching the user
        setFile("usersAfterPatch", "12\n15\n20\n19\n");
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("users", "usersAfterPatch")) << "Comparison for " << inputs[i] << " failed!";
    }
}

    // Test for adding a new movie for existing user
TEST(PatchExecuteTest, UserIdFoundAddingMovie) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4");
    const string inputs[] = {"11", "1001", "150", "100100"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs); ++i) {
        // Creates the watch list of user 1
        setFile("1_watchlist", "100\n101\n102\n103");
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("PATCH 1 "+ inputs[i], "204 No Content"));
        // Create a file to store the result as after adding the movie
        setFile("watchListAfterAdd", "100\n101\n102\n103\n" + inputs[i]);
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("1_watchlist", "watchListAfterAdd")) << "Comparison for " << inputs[i] << " failed!";
    }
}

    // Test for not adding a movie that already exist
TEST(PatchExecuteTest, UserIdFoundMovieExists) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4");
    // Creates the watch list of user 1
    setFile("1_watchlist", "100\n101\n102\n103");
    const string inputs[] = {"100", "101", "102", "103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs); ++i) {
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("PATCH 1 "+ inputs[i], "404 Not Found"));
        // Create a file to store the previous watchlist
        setFile("watchListAfterAdd", "100\n101\n102\n103");
        // Compare the "before" and "after" files to validate there are no changes - not suppose to add any movie
        compareFiles("1_watchlist", "watchListAfterAdd");
    }
}

    // Test for not adding a movie that was repeated
TEST(PatchExecuteTest, UserIdFoundMovieRepeats) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    const string inputs[] = {"104", "105"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs); ++i) {
        // Creates the watch list of user 1
        setFile("1_watchlist", "100\n101\n102\n103");
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("PATCH 1 150 " + inputs[i] + " " + inputs[i], "204 No Content"));
        // Create a file to store the the result as after adding the correct movies
        setFile("watchListAfterAdd", "100\n101\n102\n103\n150\n" + inputs[i]);
        // Compare the "correct" and "tested" files to validate the difference - suppose to add just one each time (In addition to the 150 id mocie)
        ASSERT_TRUE(compareFiles("1_watchlist", "watchListAfterAdd")) << "Comparison for repeat " << inputs[i] << " failed!";
    }
}

    // Test for valid input some spaces - suppose to work normally
TEST(PatchExecuteTest, ValidInputWithSpaces) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    // Creates the watch list of user 1
    setFile("1_watchlist", "100\n101\n102\n103\n");
    // Creates a copy of the watch list of user 1
    duplicateFile("1_watchlist", "2_watchlist");
    // Execute the function with each file and similar input and check the response
    ASSERT_TRUE(checkResponseFromServer("PATCH 1 150 160 1600", "204 No Content"));
    ASSERT_TRUE(checkResponseFromServer("PATCH 2 150   160   1600", "204 No Content"));
    // Compare the files to validate the changes - suppose to behave the same
    ASSERT_TRUE(compareFiles("1_watchlist", "2_watchlist")) << "Comparison for spaces failed!";
}

TEST(AddExecuteTest, invalidInputs) {
     // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    // Creates the watch list of user 1
    setFile("1_watchlist", "1\n2\n3\n4\n");
    // Creates a copy of the watch list of user 1
    duplicateFile("1_watchlist", "2_watchlist");
    // Array of invalid inputs to test
    const string invalidInputs[] = {"1", "-1 12", "abc", "!@#", " ", "12 ab", "1 - 2", "abc 12", "", "1-2", "1  2", "1 2 3 a b"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(invalidInputs); ++i) {
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("PATCH invalidInputs[i]", "400 Bad Request"));
        // Compare the "before" and "after" files to validate there are no changes - not suppose to add anything
        ASSERT_TRUE(compareFiles("1_watchlist", "2_watchlist")) << "Comparison for " << invalidInputs[i] << " failed!";
    }
}