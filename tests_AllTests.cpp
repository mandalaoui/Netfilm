#include <gtest/gtest.h>

//include "tests_App.cpp"
// #include "tests_Help.cpp"
#include "tests_Recommend.cpp"
// #include "tests_Add.cpp"

// Main function to initialize and run all the tests using Google Test framework
int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv); // Initialize Google Test
    return RUN_ALL_TESTS(); // Run all tests
}