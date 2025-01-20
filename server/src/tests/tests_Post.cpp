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
    const string inputs[] = {"1", "121", "115", "200"};
    Post post;
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        setFile("users","12\n15\n20\n19");
        string actualResponse = post.execute(string(inputs[i]) + " 35 20");
        // Execute the function with each input from the array and check the response
        ASSERT_EQ(actualResponse, "201 Created");        
        // Create a file to store the result as after adding the user
        setFile("usersAfterAdd", "12\n15\n20\n19\n" + string(inputs[i]));
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("users", "usersAfterPatch")) << "Comparison for " << inputs[i] << " failed!";
    }
}

    // Test for posting with existing user
TEST(PostExecuteTest, UserIdFound) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4");
    const string inputs[] = {"11", "100", "150", "103"};
    Post post;
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Creates the watch list of user 1
        setFile("1_watchlist", "100\n101\n102\n103");
        string actualResponse = post.execute("1 " + string(inputs[i]) + " 35 20");
        // Execute the function with each input from the array and check the response
        ASSERT_EQ(actualResponse, "404 Not Found");     
        // Create a file to store the result as after adding the movie
        setFile("watchListAfterAdd", "100\n101\n102\n103");
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("1_watchlist", "watchListAfterAdd")) << "Comparison for " << inputs[i] << " failed!";
    }
}

    // Test for not adding a movie that was repeated
TEST(PostExecuteTest, UserIdNotFoundMovieRepeats) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "3\n4");
    const string inputs[] = {"104", "105"};
    Post post;
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        string actualResponse = post.execute(to_string(i+100) + " 150 " + string(inputs[i]) + " " + string(inputs[i]));
        // Execute the function with each input from the array and check the response
        ASSERT_EQ(actualResponse, "201 Created") << "problem with " << string(inputs[i]) << endl;
        // Create a file to store the the result as after adding the correct movies
        setFile("watchListAfterAdd", "150\n" + inputs[i]);
        // Compare the "correct" and "tested" files to validate the difference - suppose to add just one each time (In addition to the 150 id mocie)
        ASSERT_TRUE(compareFiles(to_string(i+100) +"_watchlist", "watchListAfterAdd")) << "Comparison for repeat " << inputs[i] << " failed!";
    }
}

    // Test for valid input some spaces - suppose to work normally
TEST(PostExecuteTest, ValidInputWithSpaces) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2");
    Post post;
    string firstResponse = post.execute("300 150 160 1600");
    string secondResponse = post.execute("400 150   160   1600");
    // Execute the function with each user and similar movies and check the response
    ASSERT_EQ(firstResponse, "201 Created");
    ASSERT_EQ(secondResponse, "201 Created");
    // Compare the files to validate the changes - suppose to behave the same
    ASSERT_TRUE(compareFiles("300_watchlist", "400_watchlist")) << "Comparison for spaces failed!";
}

TEST(PostExecuteTest, invalidInputs) {
     // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4");
    // Creates the watch list of user 1
    setFile("1_watchlist", "1\n2\n3\n4");
    // Creates a copy of the watch list of user 1
    duplicateFile("1_watchlist", "2_watchlist");
    // Array of invalid inputs to test
    const string invalidInputs[] = {"1", "-1 12", "abc", "!@#", " ", "12 ab", "1 - 2", "abc 12", "", "1-2", "1   ", "1 2 3 a b"};
    Post post;
    // Loop through each input and test it
    for (int i = 0; i < sizeof(invalidInputs) / sizeof(invalidInputs[0]); ++i) {
        string actualResponse = post.execute(string(invalidInputs[i]));
        // Execute the function with each input from the array and check the response
        ASSERT_EQ(actualResponse, "400 Bad Request") << "problem with " << invalidInputs[i] << i;
        // Compare the "before" and "after" files to validate there are no changes - not suppose to add anything
        ASSERT_TRUE(compareFiles("1_watchlist", "2_watchlist")) << "Comparison for " << invalidInputs[i] << " failed!";
    }
}
