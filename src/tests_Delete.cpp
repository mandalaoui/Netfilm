#include <gtest/gtest.h>
#include <sstream>
#include "DeleteCommand.h"
#include "funcForTests.h"

using namespace std;

// Test case for deleting a movie from a user's watchlist.
TEST(DeleteExecuteTest, deleteMovieFromUser) {
    // Array of inputs representing movie IDs to delete from the watchlist.
    const string inputs[] = {"100","100 101", "100 102 103", "103 102 100 101", "103", "102 101"};
    // Expected results after deleting the specified movies
    const string results[] = {"101\n102\n103", "102\n103", "101", "", "100\n101\n102", "100\n103"};

    // Set the "users" file with user "1"
    setFile("users", "1");

    DeleteCommand deleteC;
    // Loop over all input scenarios
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); i++)
    {
        // Set the initial "1_watchlist" with a list of movies
        setFile("1_watchlist", "100\n101\n102\n103");

        string actualResponse = deleteC.execute("1 " + string(inputs[i]));

        // Call the checkResponseFromServer function to check if the server sends "204 No Content".
        ASSERT_EQ(actualResponse ,"204 No Content");

        // Set the expected result file after movie deletion.
        setFile("usersAfterDeleteMovie", results[i]);
        
        // Compare the "before" and "after" files to validate the change.
        ASSERT_TRUE(compareFiles("1_watchlist", "usersAfterDeleteMovie")) << "Comparison for " << inputs[i] << " failed!";
    }
}

// Test case to check invalid inputs of movies.
TEST(DeleteExecuteTest, invalidInputs) {

    // Array of invalid inputs of movies.
    const string inputs[] = {"100 104","100 101 %", "100 102 103 104 105", "103 AB 102"};

    // Set the "users" file with user "1"
    setFile("users", "1");

    DeleteCommand deleteC;
    // Loop through all invalid input scenarios
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); i++)
    {        
        // Set the initial "1_watchlist" with a list of movies.
        setFile("1_watchlist", "100\n101\n102\n103");

        // Duplicate the "1_watchlist" file to simulate the original state before deletion.
        duplicateFile("1_watchlist", "usersAfterDeleteInvalid");

        string actualResponse = deleteC.execute("1 " + string(inputs[i]));

        // Call the checkResponseFromServer function to check if the server sends "400 Bad Request"
        ASSERT_EQ(actualResponse ,"400 Bad Request");

        // Compare the "before" and "after" files to validate the change
        ASSERT_TRUE(compareFiles("1_watchlist", "usersAfterDeleteInvalid")) << "Comparison for " << inputs[i] << " failed!";

    }
}

// Test case to check behavior when the user does not exist.
TEST(DeleteExecuteTest, userNotExist) {
    // Array of non-existing user IDs.
    const string inputs[] = {"5", "44"}; 
    // Set the "users" file with users "1", "2", "3", and "4".
    setFile("users", "1\n2\n3\n4");

    DeleteCommand deleteC;
    // Loop through all non-existing user IDs.
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); i++)
    {   
        string actualResponse = deleteC.execute(string(inputs[i]) + " 101 102");
        // Check that the server responds with "404 Not Found" when trying to delete from a non-existent user.
        ASSERT_EQ(actualResponse , "404 Not Found");
    }
}
