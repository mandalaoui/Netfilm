#ifndef THREADPOOL_H
#define THREADPOOL_H

#include <vector>                
#include <queue>                 
#include <thread>                
#include <mutex>                 
#include <condition_variable>    
#include <functional>  
#include <atomic>

using namespace std;

class ThreadPool {
public:
    // Initializes the thread pool with a specified number of worker threads
    ThreadPool(size_t numThreads);
    // Stops the thread pool and joins all worker threads
    ~ThreadPool();

    // Submit a task to the thread pool
    void submit(function<void()> task);

private:
    // Vector of worker threads
    vector<thread> workers;
    // Queue to hold tasks to be executed by the worker threads
    queue<function<void()>> tasks;
    // Mutex to synchronize access to the task queue        
    mutex queueMutex;
    // Condition variable used to notify workers when a task is available                    
    condition_variable condition;  
    // Atomic flag to signal the thread pool to stop (ensures thread safety)         
    atomic<bool> stop;       

    // Worker thread function that continuously processes tasks from the task queue
    void workerThread();
};

#endif
