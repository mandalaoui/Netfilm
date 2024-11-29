#include <gtest/gtest.h>
#include "Help.h"
#include <sstream> 
#include "funcForTests.h"


// <sstream> Provides a memory buffer for reading/writing text, for capturing std::cout output in tests.
using namespace std;

// Test for funtcion - Execute in class - Help
// Check if the function prints the correct output
TEST(HelpExecuteTest, ExecutePrintsCorrectOutput) {
    // Create an object
    Help help;

    // Redirecting std::cout to stringstream for capturing and testing output
    stringstream buffer;
    streambuf* oldCoutBuffer = cout.rdbuf(buffer.rdbuf());

    // Run the execute function
    help.execute();

    // Restoring std::cout to its original buffer to revert to normal console output after redirection
    cout.rdbuf(oldCoutBuffer);

    // Expected output
    string expectedOutput = 
    "add [userid] [movieid1] [movieid2] …\n"
    "recommend [userid] [movieid]\n"
    "help\n";

    // Comparing the captured output with the expected output
    EXPECT_EQ(buffer.str(), expectedOutput);
    
    // Passes if they match, fails and shows differences if not.
}

/*int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}*/