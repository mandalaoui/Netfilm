#include <gtest/gtest.h>
#include "Menu.h"
#include <sstream> 
// <sstream> Provides a memory buffer for reading/writing text, for capturing std::cout output in tests.
using namespace std;

// Test for function - Execute in class - Help
TEST(MenuNextCommandTest, NextCommandPrintsCorrectOutput) {
    // create an object
    Menu menu;

    // Redirecting std::cout to stringstream for capturing and testing output
    stringstream buffer;
    streambuf* oldCoutBuffer = cout.rdbuf(buffer.rdbuf());

    // run help.execute()
    menu.nextCommand();

    // Restoring std::cout to its original buffer to revert to normal console output after redirection
    cout.rdbuf(oldCoutBuffer);

    // Expected output
    string expectedOutput = 
    "What would you like to do next?\n"
    "1. add\n"
    "2. recommend\n"
    "3. help\n";

    // Comparing the captured output with the expected output
    EXPECT_EQ(buffer.str(), expectedOutput);
    
    // Passes if they match, fails and shows differences if not.
}


// Helper function to test invalid input cases
void testInvalidInput(const string& input, const string& expectedErrorMessage) {
    // Create a Menu object
    Menu menu;

    // Redirect cin to inputBuffer and cerr to outputBuffer for capturing input/output
    stringstream inputBuffer;
    stringstream outputBuffer;
    streambuf* oldCinBuffer = cin.rdbuf(inputBuffer.rdbuf());
    streambuf* oldCerrBuffer = cerr.rdbuf(outputBuffer.rdbuf());

    // Set the inputBuffer to simulate the invalid input
    inputBuffer.str(input);  // Set the input to an invalid number (e.g., "4")

    // Run nextCommand to process the input and potentially print an error
    menu.nextCommand();

    // Restore std::cin and std::cerr to their original buffers
    cin.rdbuf(oldCinBuffer);
    cerr.rdbuf(oldCerrBuffer); 

    // Check if the output matches the expected error message
    EXPECT_EQ(outputBuffer.str(), expectedErrorMessage);
}

// Test for function - nextCommand in class - Help
TEST(MenuNextCommandTest, NextCommandReturnsValidInput) {
    // Array of invalid inputs to test
    vector<string> invalidInputs = {"4", "-1", "abc", "!@#", " ", "12", "1 2"};
    string expectedErrorMessage = "Sorry, no can do";

    // Loop through each invalid input and test it
    for (const auto& input : invalidInputs) {
        testInvalidInput(input, expectedErrorMessage);
    }
}

int main(int argc, char **argv) {
    InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}