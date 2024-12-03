#include <gtest/gtest.h>
#include <iostream>
#include <fstream>
#include "Recommend.h"
#include "funcForTests.h"

using namespace std;
// Tests for function "Execute" in class "Recommend"
    // Test case for a non-existent user.
    // Verify that the system does not produce any output.
TEST(RecommendExecuteTest, UserIdNotFound) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "12\n15\n20\n19\n");
    
    Recommend recommend;
    const char* inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Redirect cout to a stringstream to capture output
        ostringstream capturedOutput;
        streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
        
        // Execute the function with each input from the array - not suppose to print anything
        recommend.execute(string(inputs[i]) + " 35");
        
        // Restore original cout buffer
        cout.rdbuf(originalCoutBuffer);
        
        // Verify that no output was printed
        EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << inputs[i] << endl;
    }
}
    // Test case for a non-existent movie.
    // Verify that the system does not produce any output.
TEST(RecommendExecuteTest, MovieIdNotFound) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    // Creates the watch list of user 1
    setFile("1_watchlist", "100\n101\n102\n103\n");
        
    Recommend recommend;
    const char* inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Redirect cout to a stringstream to capture output
        ostringstream capturedOutput;
        streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
        
        // Execute the function with each input from the array - not suppose to print anything
        recommend.execute("1 "+ string(inputs[i]));
        
        // Restore original cout buffer
        cout.rdbuf(originalCoutBuffer);
        
        // Verify that no output was printed
        EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << inputs[i] << endl;
    }
}
    // Test case to verify that the Execute function returns the correct movie recommendation.
TEST(RecommendExecuteTest, ExecuteReturnsCorrectRecommendation) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n4\n");
    // Create watchlists for each user
    setFile("1_watchlist", "100\n101\n102\n103\n"); // User 1's watchlist
    setFile("2_watchlist", "100\n101\n102\n110\n"); // User 2's watchlist
    setFile("3_watchlist", "100\n105\n107\n110\n"); // User 3's watchlist
    setFile("4_watchlist", "105\n102\n109\n"); // User 4's watchlist

    // Define expected recommendations for each movie watched by User 1
    string recommendations[] = {"110 105 107\n", "110\n", "110 105 109\n", ""};
    
    Recommend rcommend;
    const char* inputs[] = {"100", "101", "102, 103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Redirect cout to a stringstream to capture output
        ostringstream capturedOutput;
        streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
        
        // Execute the function for User 1 with the current movie ID from inputs[]
        rcommend.execute("1 " + string(inputs[i]));
        
        // Restore original cout buffer
        cout.rdbuf(originalCoutBuffer);
        
        cout << "For input: 1 " + string(inputs[i])+"\n";
        // Verify that the output matches the expected recommendations
        EXPECT_EQ(capturedOutput.str(), recommendations[i]);
    }
}
    // Test case to ensure that the function returns no more than ten recommendations
TEST(RecommendExecuteTest, NoMoreThanTenRecommendations) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n");
    // Create watchlist for user 1 (with 4 movies)
    setFile("1_watchlist", "100\n101\n102\n103\n");
    // Create watchlist for user 2 (with more than 10 movies)
    setFile("2_watchlist", "99\n101\n102\n104\n105\n106\n107\n108\n109\n110\nn111\n112\nn113\n114\n");

    Recommend recommend;
    // Define the set of movie IDs to test the recommendations for
    const char* inputs[] = {"101", "102"};
    const char* correctRecommendations[] = {"99\n102\n104\n105\n106\n107\n108\n109\n110\nn111\n"
                                            "99\n101\n104\n105\n106\n107\n108\n109\n110\nn111\n"};

    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Redirect cout to a stringstream to capture output
        ostringstream capturedOutput;
        streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());

        // Execute the function with each input from the array for user 1
        recommend.execute("1 " + string(inputs[i]));
        
        // Restore original cout buffer
        cout.rdbuf(originalCoutBuffer);

        // Compare the current watchlist after adding recommendations with the expected result
        cout << "For input: 1 " + string(inputs[i])+"\n";
        EXPECT_EQ(capturedOutput.str(), correctRecommendations[i]);
    }
}
    // Test case to ensure that the function does not modify any file
TEST(RecommendExecuteTest, ExecuteDoesntChangeFiles) {
    // Initialize the "users" file (clear or create it)
    setFile("users", "1\n2\n3\n");
    // Create watchlists for each user
    setFile("1_watchlist", "100\n101\n102\n103\n"); // User 1's watchlist
    setFile("2_watchlist", "101\n102\n107\n108\n"); // User 2's watchlist
    setFile("3_watchlist", "101\n106\n109\n110\n"); // User 3's watchlist
    
    Recommend recommend;
    // Define the set of movie IDs to test the recommendations for user 1
    const char* inputs[] = {"100", "101", "102", "103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Execute the recommendation function for user 1 and the current movie ID
        recommend.execute("1 " + string(inputs[i]));
        
        // Verify that user 1's watchlist remains unchanged after the recommendation
        setFile("1_watchListAfterReco", "100\n101\n102\n103\n");
        ASSERT_TRUE(compareFiles("1_watchlist", "1_watchListAfterReco")) << "Comparison for users1 failed!";

        // Verify that user 2's watchlist remains unchanged after the recommendation
        setFile("2_watchListAfterReco", "100\n101\n102\n103\n");
        ASSERT_TRUE(compareFiles("2_watchlist", "2_watchListAfterReco")) << "Comparison for users2 failed!";

        // Verify that user 3's watchlist remains unchanged after the recommendation
        setFile("3_watchListAfterReco", "100\n101\n102\n103\n");
        ASSERT_TRUE(compareFiles("3_watchlist", "3_watchListAfterReco")) << "Comparison for users3 failed!";
    }
}
    // Test case to verify that the function handles valid input with extra spaces correctly
TEST(RecommendExecuteTest, ValidInputWithSpaces) {
    // Create watchlist for user 1
    setFile("1_watchlist", "100\n101\n102\n103\n");
    // Create watchlist for user 2
    setFile("2_watchlist", "100\n103\n110\n");

    Recommend recommend;

    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutput1;
    streambuf* originalCoutBuffer1 = cout.rdbuf(capturedOutput1.rdbuf());
    // Test recommendation for user 1 with a valid input
    recommend.execute("1 100");
    // Restore original cout buffer
    cout.rdbuf(originalCoutBuffer1);

    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutput2;
    streambuf* originalCoutBuffer2 = cout.rdbuf(capturedOutput2.rdbuf());
    // Test recommendation for the duplicated file with excessive spaces in the input
    recommend.execute("1      100"); //duplicate
    // Restore original cout buffer
    cout.rdbuf(originalCoutBuffer2);

    // Compare the current watchlist after adding recommendations with the expected result
    EXPECT_EQ(capturedOutput1.str(), capturedOutput1.str());
}
    // Test case to verify that the function handles invalid inputs without altering watchlist
TEST(RecommendExecuteTest, invalidInputs) {
    // Create watchlist for user 1
    setFile("1_watchlist", "1\n2\n3\n4\n");
    // Duplicate the watchlist to compare with after running invalid inputs
    duplicateFile("1_watchlist", "2_watchlist");
    
    Recommend recommend;
    // Array of invalid inputs to test
    const char* invalidInputs[] = {"4", "-1 12", "abc", "!@#", " ", "1 ab", "1 - 2", "abc 12", "", "1-2",
                                     "1  2", "1 2 3 a b", "2 12 12", "1 2 3 4", "  35"};

    // Loop through each input and test it
    for (int i = 0; i < sizeof(invalidInputs) / sizeof(invalidInputs[0]); ++i) {
        // Redirect cout to a stringstream to capture output
        ostringstream capturedOutput;
        streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
        // Execute the function with the invalid input
        recommend.execute(string(invalidInputs[i]));
        // Restore original cout buffer
        cout.rdbuf(originalCoutBuffer);

        // Verify that the watchlist remains unchanged
        ASSERT_TRUE(compareFiles("1_watchlist", "2_watchlist")) << "Comparison for " << invalidInputs[i] << " failed!";
        // Verify that no output was printed
        EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << invalidInputs[i] << endl;
    }
}