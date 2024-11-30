#include <gtest/gtest.h>
#include <fstream>
#include "App.h"
#include "Help.h"
#include "Recommend.h"
#include "Add.h"
#include "funcForTests.h"

using namespace std;

// Global map linking command names to their respective ICommand objects
map<std::string, ICommand*> commands = {
    {"help", new Help()},
    {"recommend", new Recommend()},
    {"add", new Add()}
};
// Test for class "App"
    // Test case to verify that no output is produced for invalid inputs
TEST(AppRunTest, NoOutputForInvalidInput) {
    const char* InvalidInput[] = {"help 12", "hep", "helpp", "he lp", "help1"
                                    "add 12", "addd", "add 15 a", "add1 12 12"
                                    "reccome 15 15", "recommend 10 10", "recommend22"
                                    "hi, help", "1add 15 15", "-recommend 10 10"};
    
    // Loop through each input and test it
    for (int i = 0; i < sizeof(InvalidInput) / sizeof(InvalidInput[0]); ++i) {
    // Save the original cin buffer
    streambuf* originalCinBuffer = cin.rdbuf();

    // Simulate input by redirecting cin to read from a stringstream
    istringstream simulatedInput(string(InvalidInput[i]));
    cin.rdbuf(simulatedInput.rdbuf());

    App app(commands);
    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutput;
    streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
    
    // Execute the function with the current input
    app.run();

    // Restore the original cin buffer
    cin.rdbuf(originalCinBuffer);
        
    // Restore original cout buffer
    cout.rdbuf(originalCoutBuffer);
        
    // Verify that no output was produced for the invalid input
    EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << InvalidInput[i];
    }
}
    // Test case to ensure files haven't changed for invalid input
TEST(AppRunTest, NoChangeForInvalidInput) {
    // Array of invalid inputs to test
    const char* InvalidInput[] = {"help 12", "hep", "helpp", "he lp"
                                    "add 12", "addd", "add 15 a"
                                    "reccome 15 15", "recommend 10 10", "recommend22"
                                    "hi, help", "1add 15 15", "-recommend 10 10"};
    // Initialize the "users" file with a set of test users    
    setFile("users", "1\n2\n3\n");
    // Initialize watchlists for users    
    setFile("1_watchlist", "100\n101\n102\n103\n");
    setFile("2_watchlist", "101\n102\n107\n108\n");
    setFile("3_watchlist", "101\n106\n109\n110\n");
    // Duplicate watchlists for comparing later
    duplicateFile("1_watchlist", "1_watchListAfterAdd");
    duplicateFile("2_watchlist", "2_watchListAfterAdd");
    duplicateFile("3_watchlist", "3_watchListAfterAdd");

    // Save the original cin buffer to restore it later    
    streambuf* originalCinBuffer = cin.rdbuf();
    // Loop through each input and test it
    for (int i = 0; i < sizeof(InvalidInput) / sizeof(InvalidInput[0]); ++i) {
    
    // Simulate input by redirecting cin to a stringstream with the current invalid input
    istringstream simulatedInput(string(InvalidInput[i]));
    cin.rdbuf(simulatedInput.rdbuf());

    // Create an instance of the App class with the predefined commands map
    App app(commands);    
    // Execute the application run with the simulated input
    app.run();
    // Restore the original cin buffer to avoid affecting other tests
    cin.rdbuf(originalCinBuffer);

    cout << "For input: 1 " + string(InvalidInput[i])+"\n";
    // Verify that the first user's watchlist remains unchanged
    compareFiles("1_watchlist", "1_watchListAfterAdd");
    // Verify that the second user's watchlist remains unchanged
    compareFiles("2_watchlist", "2_watchListAfterAdd");
    // Verify that the third user's watchlist remains unchanged
    compareFiles("3_watchlist", "3_watchListAfterAdd");
    }
}

    // Test case to ensure the "Help" command produces the correct output
TEST(AppRunTest, runCallsHelpOutput) {
    // Save the original buffer for input
    streambuf* originalCinBuffer = cin.rdbuf();

    // Define a set of inputs to simulate user input after "Help"
    const char* inputs[] = {"", " 10", " a"};

    // Loop through each input to simulate user behavior
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
    // Create a stringstream to simulate user input
    istringstream simulatedInput("help" + string(inputs[i]));
    cin.rdbuf(simulatedInput.rdbuf());

    App app(commands);
    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutput;
    streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
    
    // Execute the function with the current input
    app.run();

    // Restore the original input and output buffers after running the App
    cin.rdbuf(originalCinBuffer);
    cout.rdbuf(originalCoutBuffer);

    // Save the buffer for a second test with Help directly
    streambuf* secondCinBuffer = cin.rdbuf();

    // Create a Help object to call its execute function directly
    Help help;
    help.execute("help");

    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutputFromHelp;
    streambuf* secondCoutBuffer = cout.rdbuf(capturedOutputFromHelp.rdbuf());

    // Restore the cin buffer to ensure it reflects Help's output
    cin.rdbuf(secondCinBuffer);
        
    // Restore the output buffer after Help finishes execution
    cout.rdbuf(secondCoutBuffer);

    // Compare the output of running the App's Help command and the direct Help command
    EXPECT_EQ(capturedOutput.str(), capturedOutputFromHelp.str());
    }
}
    // Test case to verify that the "Help" command does not modify any files
TEST(AppRunTest, runCallsHelpFiles) {  
    // Set up initial files for users and their watchlists
    setFile("users", "1\n2\n3\n");
    setFile("1_watchlist", "100\n101\n102\n103\n"); // User 1's watchlist
    setFile("2_watchlist", "101\n102\n107\n108\n"); // User 2's watchlist
    setFile("3_watchlist", "101\n106\n109\n110\n"); // User 3's watchlist
    // Create backup copies of the watchlists to compare later
    duplicateFile("1_watchlist", "1_watchListAfterAdd");
    duplicateFile("2_watchlist", "2_watchListAfterAdd");
    duplicateFile("3_watchlist", "3_watchListAfterAdd");

    // Save the original cin buffer to restore later
    streambuf* originalCinBuffer = cin.rdbuf();
    
    // Simulate the user input for the "Help" command
    istringstream simulatedInput("help");
    cin.rdbuf(simulatedInput.rdbuf());

    App app(commands);    
    // Execute the function (which processes the "Help" command)
    app.run();
    // Restore the original cin buffer
    cin.rdbuf(originalCinBuffer);

    // Compare the current state of the watchlists with their backups to ensure no changes
    cout << "For input: help\n";
    compareFiles("1_watchlist", "1_watchListAfterAdd");
    compareFiles("2_watchlist", "2_watchListAfterAdd");
    compareFiles("3_watchlist", "3_watchListAfterAdd");
}

    // Test case to verify that the "Add" command behaves correctly and produces no output
TEST(AppRunTest, runCallsAddOutput) {
    // Set up initial files for users and their watchlists
    setFile("users", "1\n");
    setFile("1_watchlist", "100\n101\n");

    // Capture the output produced by the "Add" command
    ostringstream capturedOutput;

    // Redirect cout to capture any output from the app
    streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());

    App app(commands);
    // Define a list of simulated inputs for the "Add" command
    const char* inputs[] = {"100", "105", "101 102", "105 106"};

    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {

    // Simulate user input for the "Add" command
    istringstream simulatedInput("add " + string(inputs[i]) + "\n");
    cin.rdbuf(simulatedInput.rdbuf());

    // Execute the App's run method to process the simulated input
    app.run();

    // Restore original cout buffer
    cout.rdbuf(originalCoutBuffer);

    // Verify that no output was printed
    EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << inputs[i];
    }
}
    // Test case to verify that the "Add" command correctly updates files
TEST(AppRunTest, runCallsAddFiles) {
    // Set up initial files: users and their respective watchlists
    setFile("users", "1\n");
    setFile("1_watchlist", "100\n101\n");
    
    // Capture the original cin buffer to restore later
    streambuf* originalCinBuffer = cin.rdbuf();

    // Array of simulated inputs for the "Add" command
    const char* inputs[] = {"1 100", "1 100 99", "1 109", "1 100 101"};
    // Array of expected content for the watchlist after each "Add" operation
    const char* inputsAdded[] = {"100\n101\n", "100\n101\n99\n", "100\n101\n109\n", "100\n101\n"};

    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {

    // Simulate the user input for the "Add" command
    istringstream simulatedInput("add " + string(inputs[i]) + "\n");
    cin.rdbuf(simulatedInput.rdbuf());

    App app(commands);    
    // Execute the function with the current input
    app.run();
    // Restore the original cin buffer
    cin.rdbuf(originalCinBuffer);

    // Compare the resulting watchlist with the expected outcome for each test case
    cout << "For input: add " + string(inputs[i])+"\n";
    compareFiles("1_watchlist", string(inputsAdded[i]));
    compareFiles("2_watchlist",  string(inputsAdded[i]));
    compareFiles("3_watchlist",  string(inputsAdded[i]));
    }
}
   
    // Test case to verify that the "Recommend" command behaves as expected and produces the correct output
TEST(AppRunTest, runCallsRecommendOutput) {
    // Set up initial files: users and their respective watchlists
    setFile("users", "1\n2\n3\n");
    setFile("1_watchlist", "100\n101\n");
    setFile("2_watchlist", "101\n102\n103\n104\n110\n111\n");
    setFile("3_watchlist", "100\n101\n102\n103\n104\n");

    // Create a stringstream to capture the output of the App execution
    ostringstream capturedOutput;
    // Capture the original cout buffer to restore it later
    streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());

    App app(commands);

    // Array of simulated inputs for the "Recommend" command, specifying the user and the movie ID
    const char* inputs[] = {"1 101", "2 104", "1 100"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {

    // Simulate user input for the "Recommend" command
    istringstream simulatedInput("recommend " + string(inputs[i]) + "\n");
    cin.rdbuf(simulatedInput.rdbuf());

    app.run();

    // Restore original cout buffer
    cout.rdbuf(originalCoutBuffer);

    // Create a new instance of Recommend and execute the same command to compare outputs
    streambuf* secondCinBuffer = cin.rdbuf();
    Recommend recommend;
    recommend.execute(string(inputs[i]));

    // Redirect cout to a stringstream to another capture output
    ostringstream capturedOutputFromReco;
    streambuf* secondCoutBuffer = cout.rdbuf(capturedOutputFromReco.rdbuf());

    // Restore cin buffer and execute the Recommend command
    cin.rdbuf(secondCinBuffer);
    cout.rdbuf(secondCoutBuffer);

    // Compare the captured outputs: the one from App's run() method and the one from Recommend's execute()
    EXPECT_EQ(capturedOutput.str(), capturedOutputFromReco.str());
    }   
}
    // Test case to verify that the "Recommend" command modifies the watchlists as expected
TEST(AppRunTest, runCallsRecommendFiles) {
    // Set up initial files: users and their respective watchlists
    setFile("users", "1\n2\n3\n");
    setFile("1_watchlist", "100\n101\n102\n103\n"); // User 1's watchlist
    setFile("2_watchlist", "101\n102\n107\n108\n"); // User 2's watchlist
    setFile("3_watchlist", "101\n106\n109\n110\n"); // User 3's watchlist
    
    // Create duplicates of the watchlists before modifications (used for comparison later)
    duplicateFile("1_watchlist", "1_watchListAfterAdd");
    duplicateFile("2_watchlist", "2_watchListAfterAdd");
    duplicateFile("3_watchlist", "3_watchListAfterAdd");

    // Capture the original cin buffer to restore it later
    streambuf* originalCinBuffer = cin.rdbuf();

    // Array of simulated inputs
    const char* inputs[] = {"1 100", "2 102", "3 109"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {

    // Simulate user input for the "Recommend" command (e.g., "Recommend 1 100")
    istringstream simulatedInput("recommend " + string(inputs[i]) + "\n");
    cin.rdbuf(simulatedInput.rdbuf());

    App app(commands);    
    // Execute the function with the current input
    app.run();

    // Restore the original cin buffer after running the command
    cin.rdbuf(originalCinBuffer);

    // Compare the updated watchlists with the expected outputs after the "Recommend" command
    cout << "For input: recommend " + string(inputs[i])+"\n";
    compareFiles("1_watchlist", "1_watchListAfterAdd");
    compareFiles("2_watchlist", "2_watchListAfterAdd");
    compareFiles("3_watchlist", "3_watchListAfterAdd");
    }
}