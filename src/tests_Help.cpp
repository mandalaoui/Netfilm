#include <gtest/gtest.h>
#include <sstream>
#include "Help.h"
#include "funcForTests.h"

// <sstream> Provides a memory buffer for reading/writing text, for capturing std::cout output in tests.
using namespace std;

// Test for funtcion "Execute" in class "Help"
// Check if the server sends the correct output to the client
TEST(HelpExecuteTest, ExecutePrintsCorrectOutput) {
    // Create an object
    Help help;
    // Run the execute function
    string actualResponse = help.execute("help");
    
    // Expected output
    string expectedResponse = 
    "DELETE, arguments: [userid] [movieid1] [movieid2] ...\n"
    "GET, arguments: [userid] [movieid]\n"
    "PATCH, arguments: [userid] [movieid1] [movieid2] ...\n"
    "POST, arguments: [userid] [movieid1] [movieid2] ...\n"
    "help";

    // Comparing the captured output with the expected output
    ASSERT_EQ(actualResponse, expectedResponse);
    // Passes if they match, fails and shows differences if not.
}