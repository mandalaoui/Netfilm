#include <gtest/gtest.h>
#include "App.h"

using namespace std;

TEST(AdditionTest, PositiveNumbers) {
    EXPECT_EQ(add(1,2), 3);
}

int main(int argc, char **argv) {
    InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}