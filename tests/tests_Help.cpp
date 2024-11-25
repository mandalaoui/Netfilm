#include <gtest/gtest.h>
#include "Help.h"
#include <sstream> 
// <sstream> Provides a memory buffer for reading/writing text, for capturing std::cout output in tests.

// Test for funcion - Execute in class - Help
TEST(HelpExecuteTest, ExecutePrintsCorrectOutput) {
    // create an object
    Help help;

    // Redirecting std::cout to stringstream for capturing and testing output
    std::stringstream buffer;
    std::streambuf* oldCoutBuffer = std::cout.rdbuf(buffer.rdbuf());

    // run help.execute()
    help.execute();

    // Restoring std::cout to its original buffer to revert to normal console output after redirection
    std::cout.rdbuf(oldCoutBuffer);

    // Expected output
    std::string expectedOutput = 
    "add [userid] [movieid1] [movieid2] …\n"
    "recommend [userid] [movieid]\n"
    "help\n";

    // Comparing the captured output with the expected output
    EXPECT_EQ(buffer.str(), expectedOutput);
    
    // Passes if they match, fails and shows differences if not.
}

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}