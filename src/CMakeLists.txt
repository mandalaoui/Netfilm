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

include_directories(${CMAKE_SOURCE_DIR}/commands 
                    ${CMAKE_SOURCE_DIR}/tests 
                    ${CMAKE_SOURCE_DIR}/utils 
                    ${CMAKE_SOURCE_DIR}/system 
                    ${CMAKE_SOURCE_DIR}/threads
                    ${CMAKE_SOURCE_DIR}/client
                    ${CMAKE_SOURCE_DIR}/interfaces)

add_executable(runProject main.cpp 
    commands/DeleteCommand.cpp 
    commands/Patch.cpp 
    commands/Add.cpp 
    commands/Post.cpp 
    commands/Help.cpp 
    commands/Recommend.cpp 
    commands/MapCommands.cpp 
    system/Server.cpp 
    system/ClientHandle.cpp 
    system/AnExecutor.cpp 
    threads/LockerThread.cpp 
    utils/dataFuncs.cpp)

add_executable(runTests 
    tests/tests_AllTests.cpp 
    commands/Add.cpp 
    commands/Post.cpp 
    commands/Patch.cpp 
    commands/Help.cpp 
    commands/Recommend.cpp 
    commands/DeleteCommand.cpp 
    threads/LockerThread.cpp 
    utils/funcForTests.cpp 
    utils/dataFuncs.cpp)

target_link_libraries(runTests gtest_main)
