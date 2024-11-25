#include <gtest/gtest.h>
#include "Add.h"

using namespace std;

TEST(AddExecuteTest, UserIdNotFoundCreateNew) {
    //
}

TEST(AddExecuteTest, UserIdFoundAddingMovie) {
    //
}

TEST(AddExecuteTest, UserIdFoundMovieExists) {
    //
}

TEST(AddExecuteTest, UserIdFoundMovieRepeats) {
    //
}

// Helper function to test invalid input cases
void testInvalidInput(const string& input, const string& expectedErrorMessage) {
    // Create a Menu object
    Add add;

    // Redirect cin to inputBuffer and cerr to outputBuffer for capturing input/output
    stringstream inputBuffer;
    stringstream outputBuffer;
    streambuf* oldCinBuffer = cin.rdbuf(inputBuffer.rdbuf());
    streambuf* oldCerrBuffer = cerr.rdbuf(outputBuffer.rdbuf());

    // Set the inputBuffer to simulate the invalid input
    inputBuffer.str(input);  // Set the input to an invalid number (e.g., "4")

    // Run nextCommand to process the input and potentially print an error
    Add.Execute();

    // Restore std::cin and std::cerr to their original buffers
    cin.rdbuf(oldCinBuffer);
    cerr.rdbuf(oldCerrBuffer); 

    // Check if the output matches the expected error message
    EXPECT_EQ(outputBuffer.str(), expectedErrorMessage);
}

// Test for function - Execute in class - Add
// Check the function's behavior for invalid inputs
TEST(AddExecuteTest, invalidInputs) {
// Array of invalid inputs to test
    const char* invalidInputs[] = {"4", "-1 12", "abc", "!@#", " ", "12 ab", "1 - 2", "abc 12", "", "1-2", "1  2", "1 2 3 a b"};
    string expectedErrorMessage = "Sorry, no can do";

    // Loop through each invalid input and test it
    for (int i = 0; i < sizeof(invalidInputs) / sizeof(invalidInputs[0]); ++i) {
        testInvalidInput(invalidInputs[i], expectedErrorMessage);
    }
}


int main(int argc, char **argv) {
    InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}