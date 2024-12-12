#include <gtest/gtest.h>
#include <iostream>
#include <fstream>
#include "Post.h"
#include "funcForTests.h"

using namespace std;
// Tests for function "Execute" in class "Post"

    // Test for adding a new user
TEST(PostExecuteTest, UserIdNotFoundCreateNew) {
    // Initialize the "users" file (clear or create it)   
    const string inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs); ++i) {
        setFile("users","12\n15\n20\n19\n");
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("POST "+ inputs[i] + " 35 20", "201 Created"));
        // Create a file to store the result as after adding the user
        setFile("usersAfterAdd", "12\n15\n20\n19\n" + inputs[i]);
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("users", "usersAfterPatch")) << "Comparison for " << inputs[i] << " failed!";
    }
}

    // Test for posting with existing user
TEST(PostExecuteTest, UserIdFound) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    const string inputs[] = {"11", "100", "150", "103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs); ++i) {
        // Creates the watch list of user 1
        setFile("1_watchlist", "100\n101\n102\n103\n");
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("PATCH 1 "+ inputs[i], "404 Not Found"));
        // Create a file to store the result as after adding the movie
        setFile("watchListAfterAdd", "100\n101\n102\n103\n");
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("1_watchlist", "watchListAfterAdd")) << "Comparison for " << inputs[i] << " failed!";
    }
}

    // Test for not adding a movie that was repeated
TEST(PostExecuteTest, UserIdNotFoundMovieRepeats) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    const string inputs[] = {"104", "105"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs); ++i) {
        // Creates the watch list of user 1
        setFile("1_watchlist", "100\n101\n102\n103\n");
        // Execute the function with each input from the array and check the response
        ASSERT_TRUE(checkResponseFromServer("POST 1 150 " + inputs[i] + " " + inputs[i], "201 Created"));
        // Create a file to store the the result as after adding the correct movies
        setFile("watchListAfterAdd", "100\n101\n102\n103\n150\n" + inputs[i] + "\n");
        // Compare the "correct" and "tested" files to validate the difference - suppose to add just one each time (In addition to the 150 id mocie)
        ASSERT_TRUE(compareFiles("1_watchlist", "watchListAfterAdd")) << "Comparison for repeat " << inputs[i] << " failed!";
    }
}

    // Test for valid input some spaces - suppose to work normally
TEST(PostExecuteTest, ValidInputWithSpaces) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n");
    // Execute the function with each user and similar movies and check the response
    ASSERT_TRUE(checkResponseFromServer("POST 3 150 160 1600", "201 Created"));
    ASSERT_TRUE(checkResponseFromServer("POST 4 150   160   1600", "201 Created"));
    // Compare the files to validate the changes - suppose to behave the same
    ASSERT_TRUE(compareFiles("3_watchlist", "4_watchlist")) << "Comparison for spaces failed!";
}

TEST(PostExecuteTest, invalidInputs) {
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
        ASSERT_TRUE(checkResponseFromServer("POST invalidInputs[i]", "400 Bad Request"));
        // Compare the "before" and "after" files to validate there are no changes - not suppose to add anything
        ASSERT_TRUE(compareFiles("1_watchlist", "2_watchlist")) << "Comparison for " << invalidInputs[i] << " failed!";
    }
}
