#include <gtest/gtest.h>

#include "tests_App.cpp"
#include "tests_Help.cpp"
#include "tests_Recommend.cpp"
#include "tests_Add.cpp"

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);  // אתחול של Google Test
    return RUN_ALL_TESTS();                   // ריצה של כל הבדיקות
}