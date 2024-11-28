#include <gtest/gtest.h>
#include "Recommend.h"
#include <iostream>
#include <fstream>

using namespace std;

bool searchInFile(const string& value, const string& fileName)
{
    ifstream file(fileName);
    if (!file.is_open()) {
        // std::cerr << "file not available!" << std::endl;
        return false;
    }

    string line;
    while (getline(file, line)) {
        if (line.find(value) != string::npos) {
            // value found in the file
            file.close();
            return true;
        }
    }
    file.close();
}

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

void duplicateFile(const string& sourceFile) {
    ifstream src(sourceFile, ios::binary);
    createOrClearFile("duplicate");
    ofstream dest("duplicate", ios::binary);
    if (src.is_open() && dest.is_open()) {
        dest << src.rdbuf(); // copying data to new file
        //cout << "File duplicated: " << sourceFile << " -> " << destFile << std::endl;
    } else {
        cerr << "Failed to duplicate file: " << sourceFile << " -> " << destFile << std::endl;
    }
}

void compareFiles(const string& file1, const string& file2) {
    ifstream f1(file1), f2(file2);
    if (!f1 || !f2) {
        std::cerr << "Failed to open one of the files!" << std::endl;
        return;
    }
    cout << "Comparing files:\n";
    string line1, line2;
    int lineNumber = 0;
    while (++lineNumber, std::getline(f1, line1) || std::getline(f2, line2)) {
        if (line1 != line2) {
            cout << "Difference at line " << lineNumber << ":\n"
                      << "File 1: " << line1 << "\n"
                      << "File 2: " << line2 << "\n";
        }
    }
    cout << "All done\n";
}

TEST(AddExecuteTest, UserIdNotFound) {
    createOrClearFile("users"); //usersBeforeAdd
    insertToFile("12\n15\n20\n19\n");
    
    Recommend recommend;
    const char* inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Redirect cout to a stringstream to capture output
        ostringstream capturedOutput;
        streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
        
        // Execute the function
        recommend.execute(inputs[i] + " 35");
        
        // Restore original cout buffer
        cout.rdbuf(originalCoutBuffer);
        
        // Verify that no output was printed
        EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << inputs[i];
    }
}

TEST(AddExecuteTest, MovieIdNotFound) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n4\n");

    createOrClearFile("1_watchlist"); //watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");
        
    Recommend recommend;
    const char* inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        // Redirect cout to a stringstream to capture output
        ostringstream capturedOutput;
        streambuf* originalCoutBuffer = cout.rdbuf(capturedOutput.rdbuf());
        
        // Execute the function
        recommend.execute("1 "+ inputs[i]);
        
        // Restore original cout buffer
        cout.rdbuf(originalCoutBuffer);
        
        // Verify that no output was printed
        EXPECT_EQ(capturedOutput.str(), "") << "Unexpected output for input: " << inputs[i];
    }
}
//to check
TEST(AddExecuteTest, ExecuteReturnsCorrectRecommendation) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n4\n");
    
    createOrClearFile("1_watchlist"); //watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");

    createOrClearFile("2_watchlist"); //watchListBeforeAdd
    insertToFile("2_watchlist", "100\n101\n102\n110\n");

    createOrClearFile("3_watchlist"); //watchListBeforeAdd
    insertToFile("3_watchlist", "100\n105\n107\n110\n");

    createOrClearFile("4_watchlist"); //watchListBeforeAdd
    insertToFile("4_watchlist", "105\n102\n109\n");
    
    Add add;
    const char* inputs[] = {"100", "101", "102", "103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        add.execute("1 " + inputs[i]);
        createOrClearFile("watchListAfterAdd");
        insertToFile("watchListAfterAdd", "100\n101\n102\n103\n");

        cout << "For input: 1 " + inputs[i]+"\n";
        compareFiles("1_watchlist", "watchListAfterAdd");
    }
}
//to check
TEST(AddExecuteTest, NoMoreThanTenRecommendations) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n");
    
    createOrClearFile("1_watchlist"); //watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");

    createOrClearFile("2_watchlist"); //watchListBeforeAdd
    insertToFile("2_watchlist", "99\n101\n102\n104\n105\n106\n107\n108\n109\n110\nn111\n112\nn113\n114\n");

    Recommend recommend;
    const char* inputs[] = {"100", "101", "102"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        recommend.execute("1 " + inputs[i]);
        createOrClearFile("watchListAfterAdd");
        insertToFile("watchListAfterAdd", "99\n102\n104\n105\n106\n107\n108\n109\n110\nn111\n");

        cout << "For input: 1 " + inputs[i]+"\n";
        compareFiles("1_watchlist", "watchListAfterAdd");
    }
}

TEST(AddExecuteTest, ExecuteDoesntChangeFiles) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n");
    
    createOrClearFile("1_watchlist"); //watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");

    createOrClearFile("2_watchlist"); //watchListBeforeAdd
    insertToFile("2_watchlist", "101\n102\n107\n108\n");

    createOrClearFile("3_watchlist"); //watchListBeforeAdd
    insertToFile("3_watchlist", "101\n106\n109\n110\n");
    
    Recommend recommend;
    const char* inputs[] = {"100", "101", "102", "103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        add.execute("1 " + inputs[i]);
        
        createOrClearFile("1_watchListAfterAdd");
        insertToFile("1_watchListAfterAdd", "100\n101\n102\n103\n");

        cout << "For input: 1 " + inputs[i]+"\n";
        compareFiles("1_watchlist", "1_watchListAfterAdd");

        createOrClearFile("2_watchListAfterAdd");
        insertToFile("2_watchListAfterAdd", "100\n101\n102\n103\n");

        cout << "For input: 1 " + inputs[i]+"\n";
        compareFiles("2_watchlist", "2_watchListAfterAdd");

        createOrClearFile("3_watchListAfterAdd");
        insertToFile("3_watchListAfterAdd", "100\n101\n102\n103\n");

        cout << "For input: 1 " + inputs[i]+"\n";
        compareFiles("3_watchlist", "3_watchListAfterAdd");
    }
}

TEST(AddExecuteTest, ValidInputWithSpaces) {
    createOrClearFile("1_watchlist"); // watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");
    duplicateFile("1_watchlist"); // 000

    createOrClearFile("2_watchlist"); // watchListBeforeAdd
    insertToFile("2_watchlist", "100\n103\n110\n");

    Recommend recommend;
    recommend.execute("1 101");
    recommend.execute("000      150"); //duplicate

    compareFiles("1_watchlist", "000");
}

// Test for function - Execute in class - Add
// Check the function's behavior for invalid inputs
TEST(AddExecuteTest, invalidInputs) {
    createOrClearFile("1_watchlist"); // usersBeforeAdd
    insertToFile("1_watchlist", "1\n2\n3\n4\n");

    duplicateFile("1_watchlist"); // duplicate = "000"
    
    Recommend recommend;
    // Array of invalid inputs to test
    const char* invalidInputs[] = {"4", "-1 12", "abc", "!@#", " ", "1 ab", "1 - 2", "abc 12", "", "1-2",
                                     "1  2", "1 2 3 a b", "2 12 12", "1 2 3 4", "  35"};

    // Loop through each input and test it
    for (int i = 0; i < sizeof(invalidInputs) / sizeof(invalidInputs[0]); ++i) {
        Recommend.execute(invalidInputs[i]);
        
        // check there are no changes
        cout << "For input: " + invalidInputs[i];
        compareFiles("1_watchlist", "000");


    }
}

int main(int argc, char **argv) {  
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}