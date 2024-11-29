#include <gtest/gtest.h>
#include "Add.h"
#include <iostream>
#include <fstream>
#include "funcForTests.h"

using namespace std;

/*tests_Add::tests_Add();
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
    return false;
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
        cerr << "Failed to duplicate file: "; // << sourceFile << " -> " << dest << std::endl;
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
*/
TEST(AddExecuteTest, UserIdNotFoundCreateNew) {
    createOrClearFile("users"); //usersBeforeAdd
    insertToFile("users","12\n15\n20\n19\n");
    
    Add add;
    const char* inputs[] = {"1", "121", "115", "20"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        add.execute(string(inputs[i]) +" 35 20");
        createOrClearFile("usersAfterAdd");
        insertToFile("usersAfterAdd", "12\n15\n20\n19\n"+string(inputs[i])+"\n");

        cout << "For input: "+string(inputs[i]) +" 35 20"+"\n";
        compareFiles("usersBeforeAdd", "usersAfterAdd");
    }
}

TEST(AddExecuteTest, UserIdFoundAddingMovie) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n4\n");

    createOrClearFile("1_watchlist"); //watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");
    
    Add add;
    const char* inputs[] = {"11", "1001", "150", "100100"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        add.execute("1 " + string(inputs[i]));
        createOrClearFile("watchListAfterAdd");
        insertToFile("watchListAfterAdd", "100\n101\n102\n103\n"+string(inputs[i])+"\n");

        cout << "For input: 1 " + string(inputs[i])+"\n";
        compareFiles("1_watchlist", "watchListAfterAdd");
    }
}

TEST(AddExecuteTest, UserIdFoundMovieExists) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n4\n");
    
    createOrClearFile("1_watchlist"); //watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");
    
    Add add;
    const char* inputs[] = {"100", "101", "102", "103"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        add.execute("1 " + string(inputs[i]));
        createOrClearFile("watchListAfterAdd");
        insertToFile("watchListAfterAdd", "100\n101\n102\n103\n");

        cout << "For input: 1 " + string(inputs[i])+"\n";
        compareFiles("1_watchlist", "watchListAfterAdd");
    }
}

TEST(AddExecuteTest, UserIdFoundMovieRepeats) {
    createOrClearFile("users");
    insertToFile("users", "1\n2\n3\n4\n");

    createOrClearFile("1_watchlist"); // watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");
    
    Add add;
    const char* inputs[] = {"100", "101"};
    // Loop through each input and test it
    for (int i = 0; i < sizeof(inputs) / sizeof(inputs[0]); ++i) {
        add.execute("1 150" + string(inputs[i])+" "+ string(inputs[i]));
        createOrClearFile("watchListAfterAdd");
        insertToFile("watchListAfterAdd", "100\n101\n102\n103\n\105\n"+string(inputs[i])+"\n");

        cout << "For input: 1 150" + string(inputs[i])+" "+ string(inputs[i]);
        compareFiles("1_watchlist", "watchListAfterAdd");
    }
}

TEST(AddExecuteTest, ValidInputWithSpaces) {
    createOrClearFile("1_watchlist"); // watchListBeforeAdd
    insertToFile("1_watchlist", "100\n101\n102\n103\n");
    duplicateFile("1_watchlist");

    Add add;
    add.execute("1 150 160 1600");
    add.execute("000 150   160   1600"); //duplicate

    compareFiles("1_watchlist", "000");
}

// Test for function - Execute in class - Add
// Check the function's behavior for invalid inputs
TEST(AddExecuteTest, invalidInputs) {
    createOrClearFile("1_watchlist"); // usersBeforeAdd
    insertToFile("1_watchlist", "1\n2\n3\n4\n");

    duplicateFile("1_watchlist"); // duplicate = "000"
    
    Add add;
    // Array of invalid inputs to test
    const char* invalidInputs[] = {"1", "-1 12", "abc", "!@#", " ", "12 ab", "1 - 2", "abc 12", "", "1-2", "1  2", "1 2 3 a b"};

    // Loop through each input and test it
    for (int i = 0; i < sizeof(invalidInputs) / sizeof(invalidInputs[0]); ++i) {
        add.execute(string(invalidInputs[i]));
        
        cout << "For input: " + string(invalidInputs[i]);
        compareFiles("1_watchlist", "000");
    }
}

/*int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}*/