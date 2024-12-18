cmake_minimum_required(VERSION 3.10)

project(MyExample)

set(CMAKE_CXX_STANDARD 11)

include(FetchContent)
FetchContent_Declare(
    googletest
    URL https://github.com/google/googletest/archive/refs/tags/release-1.11.0.zip
)

# For Windows: Prevent overriding the parent project's compiler/linker settings
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

add_executable(runProject main.cpp DeleteCommand.cpp Patch.cpp Add.cpp Post.cpp Help.cpp Recommend.cpp MapCommands.cpp Server.cpp ClientHandle.cpp AnExecutor.cpp LockerThread.cpp)

add_executable(runTests tests_AllTests.cpp Add.cpp Post.cpp Patch.cpp Help.cpp Recommend.cpp DeleteCommand.cpp funcForTests.cpp LockerThread.cpp)

target_link_libraries(runTests gtest_main)
