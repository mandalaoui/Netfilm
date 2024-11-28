#include <gtest/gtest.h>
#include "App.h"
#include "Help.h"
#include "Recommend.h"
#include "Add.h"


using namespace std;


void createOrClearFile(const string& fileName) {
    ofstream file(fileName, ios::trunc); // delete mode
    if (file.is_open()) {
        //std::cout << "File created or cleared: " << fileName << std::endl;
    } else {
        std::cerr << "Failed to create or clear file: " << fileName << std::endl;
    }
}

void insertToFile(const string& fileName, const string& content) {
    ofstream file(fileName, ios::app); //adding mode
    if (file.is_open()) {
        file << content << endl;
        //std::cout << "Content added to file: " << fileName << std::endl;
    } else {
        std::cerr << "Failed to write to file: " << fileName << std::endl;
    }
}


TEST(AppRunTest, NoOutputForInvalidInput) {
    const char* InvalidInput[] = {"Help 12", "hep", "helpp", "he lp", "Help1"
                                    "Add 12", "Addd", "Add 15 a", "Add1 12 12"
                                    "Reccome 15 15", "recommend 10 10", "Recommend22"
                                    "hi, Help", "1Add 15 15", "-Recommend 10 10"};
    
    // Loop through each input and test it
    for (int i = 0; i < sizeof(InvalidInput) / sizeof(InvalidInput[0]); ++i) {
    streambuf* originalCinBuffer = cin.rdbuf();

    istringstream simulatedInput(InvalidInput[i]);
    cin.rdbuf(simulatedInput.rdbuf()); // שינוי ה-buffer של cin

    App app;
    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutput;
    streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
    
    // Execute the function
    app.run();

    cin.rdbuf(originalCinBuffer);
        
    // Restore original cout buffer
    cout.rdbuf(originalCoutBuffer);
        
    // Verify that no output was printed
    EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << InvalidInput[i];
    }
}

TEST(AppRunTest, NoChangeForInvalidInput) {
    const char* InvalidInput[] = {"Help 12", "hep", "helpp", "he lp"
                                    "Add 12", "Addd", "Add 15 a"
                                    "Reccome 15 15", "recommend 10 10", "Recommend22"
                                    "hi, Help", "1Add 15 15", "-Recommend 10 10"};
    
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n");
    
    createOrClearFile("1_watchlist"); //watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");

    createOrClearFile("2_watchlist"); //watchListBeforeAdd
    insertToFile("2_watchlist", "101\n102\n107\n108\n");

    createOrClearFile("3_watchlist"); //watchListBeforeAdd
    insertToFile("3_watchlist", "101\n106\n109\n110\n");
    
    // Loop through each input and test it
    for (int i = 0; i < sizeof(InvalidInput) / sizeof(InvalidInput[0]); ++i) {
    streambuf* originalCinBuffer = cin.rdbuf();

    istringstream simulatedInput(InvalidInput[i]);
    cin.rdbuf(simulatedInput.rdbuf()); // שינוי ה-buffer של cin

    App app;
    
    // Execute the function
    app.run();

    cin.rdbuf(originalCinBuffer);

    //compare      
    createOrClearFile("1_watchListAfterAdd");
    insertToFile("1_watchListAfterAdd", "100\n101\n102\n103\n");

    cout << "For input: 1 " + InvalidInput[i]+"\n";
    compareFiles("1_watchlist", "1_watchListAfterAdd");

    createOrClearFile("2_watchListAfterAdd");
    insertToFile("2_watchListAfterAdd", "100\n101\n102\n103\n");

    cout << "For input: 1 " + InvalidInput[i]+"\n";
    compareFiles("2_watchlist", "2_watchListAfterAdd");

    createOrClearFile("3_watchListAfterAdd");
    insertToFile("3_watchListAfterAdd", "100\n101\n102\n103\n");

    cout << "For input: 1 " + InvalidInput[i]+"\n";
    compareFiles("3_watchlist", "3_watchListAfterAdd");
    }
}

// check if the input is "Help", behave like Help
//check output
TEST(AppRunTest, runCallsHelpOutput) {
    streambuf* originalCinBuffer = cin.rdbuf();

    const char* inputs[] = {"", " 10", " a"};

    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
    istringstream simulatedInput("Help" + inputs[i]);
    cin.rdbuf(simulatedInput.rdbuf()); // שינוי ה-buffer של cin

    App app;
    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutput;
    streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
    
    // Execute the function
    app.run();

    cin.rdbuf(originalCinBuffer);
        
    // Restore original cout buffer
    cout.rdbuf(originalCoutBuffer);

    streambuf* secondCinBuffer = cin.rdbuf();

    Help help;
    help.execute();

    // Redirect cout to a stringstream to capture output
    ostringstream capturedOutputFromHelp;
    streambuf* secondCoutBuffer = cout.rdbuf(capturedOutputFromHelp.rdbuf());

    cin.rdbuf(secondCinBuffer);
        
    // Restore original cout buffer
    cout.rdbuf(secondCoutBuffer);

    // Verify that no output was printed
    EXPECT_EQ(capturedOutput.str(), capturedOutputFromHelp.str()) << "Unexpected output for input: " << inputs[i];
    }
}
// check files
TEST(AppRunTest, runCallsHelpFiles) {

}
// check if the input is "Add", behave like Add
//check output
TEST(AppRunTest, runCallsAddOutput) {
    
}
// check files
TEST(AppRunTest, runCallsAddFiles) {

}
// check if the input is "Recommend", behave like Recommend
//check output
TEST(AppRunTest, runCallsRecommendOutput) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n");

    createOrClearFile("1_watchlist"); 
    insertToFile("2_watchlist", "100\n101\n");

    createOrClearFile("2_watchlist"); 
    insertToFile("2_watchlist", "101\n102\n103\n104\n110\n111\n");

    createOrClearFile("3_watchlist"); 
    insertToFile("3_watchlist", "100\n101\n102\n103\n104\n");

    ostringstream capturedOutput;

    streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());

    App app;

    const char* inputs[] = {"1 101", "2 104", "1 100"};

    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {

    // יצירת קלט מדומה
    istringstream simulatedInput("Recommend " + input[i] + "\n");
    cin.rdbuf(simulatedInput.rdbuf()); // שינוי ה-buffer של cin

    // קריאה לפונקציה שלך
    app.run();

    // החזרת ה-buffer המקורי
    cin.rdbuf(originalCinBuffer);

    }


    
}
// check files
TEST(AppRunTest, runCallsRecommendFiles) {
    
}

int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}