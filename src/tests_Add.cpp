#include <gtest/gtest.h>
#include <iostream>
#include <fstream>
#include "Add.h"
#include "funcForTests.h"

using namespace std;
// Tests for function "Execute" in class "Add"
    // Test for adding a new user (if the user is not found, a new one will be created)
TEST(AddExecuteTest, UserIdNotFoundCreateNew) {
    // Initialize the "users" file (clear or create it)   
    Add add;
    const char* inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        setFile("users","12\n15\n20\n19");
        // Execute the function with each input from the array - suppose to add a new user
        add.execute(string(inputs[i]) +" 35 20");
        // Create a file to store the result as after adding the user
        setFile("usersAfterAdd", "12\n15\n20\n19\n"+string(inputs[i]));
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("users", "usersAfterAdd")) << "Comparison for " << string(inputs[i]) << " failed!";
    }
}
    // Test for adding a new movie for existing user
TEST(AddExecuteTest, UserIdFoundAddingMovie) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4");
    // Creates the watch list of user 1
    Add add;
    const char* inputs[] = {"11", "1001", "150", "100100"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        setFile("1_watchlist", "100\n101\n102\n103");
        // Execute the function with each input from the array - suppose to add a new movie
        add.execute("1 " + string(inputs[i]));
        // Create a file to store the result as after adding the movie
        setFile("watchListAfterAdd", "100\n101\n102\n103\n"+string(inputs[i]));
        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("1_watchlist", "watchListAfterAdd")) << "Comparison for " << (inputs[i]) << " failed!";
    }
}
    // Test for not adding a movie that already exist
TEST(AddExecuteTest, UserIdFoundMovieExists) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4");
    // Creates the watch list of user 1
    setFile("1_watchlist", "100\n101\n102\n103");
        
    Add add;
    const char* inputs[] = {"100", "101", "102", "103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Execute the function with each input from the array - not suppose to add any movie
        add.execute("1 " + string(inputs[i]));
        // Create a file to store the previous watchlist
        setFile("watchListAfterAdd", "100\n101\n102\n103");
        // Compare the "before" and "after" files to validate there are no changes
        compareFiles("1_watchlist", "watchListAfterAdd");
    }
}
    // Test for not adding a movie that was repeated
TEST(AddExecuteTest, UserIdFoundMovieRepeats) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    // Creates the watch list of user 1
    Add add;
    const char* inputs[] = {"100", "101"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        setFile("1_watchlist", "100\n101\n102\n103");
        // Execute the function with each input from the array - suppose to add just one each time (In addition to the 150 id mocie)
        add.execute("1 150 " + string(inputs[i])+" "+ string(inputs[i]));
        // Create a file to store the the result as after adding the correct movies
        setFile("watchListAfterAdd", "100\n101\n102\n103\n150\n"+string(inputs[i]));
        // Compare the "correct" and "tested" files to validate the difference
        ASSERT_TRUE(compareFiles("1_watchlist", "watchListAfterAdd")) << "Comparison for repeat " << string(inputs[i]) << " failed!";
    }
}
    // Test for valid input some spaces - suppose to work normally
TEST(AddExecuteTest, ValidInputWithSpaces) {
    // Creates the watch list of user 1
    setFile("1_watchlist", "100\n101\n102\n103\n");
    // Creates a copy of the watch list of user 1
    duplicateFile("1_watchlist", "2_watchlist");
    
    Add add;
    // Execute the function with each file and similar input
    // Suppose to behave the same
    add.execute("1 150 160 1600");
    add.execute("2 150   160   1600"); //duplicate
    // Compare the files to validate the changes
    ASSERT_TRUE(compareFiles("1_watchlist", "2_watchlist")) << "Comparison for spaces failed!";
}
    // Test for invalid inputs - won't change anything
TEST(AddExecuteTest, invalidInputs) {
    // Creates the watch list of user 1
    setFile("1_watchlist", "1\n2\n3\n4\n");
    // Creates a copy of the watch list of user 1
    duplicateFile("1_watchlist", "2_watchlist");

    Add add;
    // Array of invalid inputs to test
    const char* invalidInputs[] = {"1", "-1 12", "abc", "!@#", " ", "12 ab", "1 - 2", "abc 12", "", "1-2", "1  2", "1 2 3 a b"};

    // Loop through each input and test it
    for (int i = 0; i < sizeof(invalidInputs) / sizeof(invalidInputs[0]); ++i) {
        // Execute the function with each input from the array - not suppose to add anything
        add.execute(string(invalidInputs[i]));
        // Compare the "before" and "after" files to validate there are no changes
        ASSERT_TRUE(compareFiles("1_watchlist", "2_watchlist")) << "Comparison for " << invalidInputs[i] << " failed!";
    }
}
