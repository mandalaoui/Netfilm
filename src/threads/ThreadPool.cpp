#include "ThreadPool.h"
using namespace std;

// Initializes the thread pool with a specified number of worker threads
ThreadPool::ThreadPool(size_t numThreads) : stop(false) {
    // Create 'numThreads' worker threads that will call 'workerThread'
    for (size_t i = 0; i < numThreads; ++i) {
        workers.emplace_back([this] { workerThread(); });
    }
}

// Stops the thread pool and joins all worker threads
ThreadPool::~ThreadPool() {
    // Set the stop flag to true to signal the threads to stop
    stop = true;
    // Notify all threads to wake up and check the stop condition                
    condition.notify_all();    

    // Join each worker thread, ensuring they complete before exiting
    for (thread &worker : workers) {
        if (worker.joinable()) {
            // Wait for each thread to finish execution
            worker.join();
        }
    }
}

// Submit a task to the thread pool
void ThreadPool::submit(function<void()> task) {
    {   
        // Lock the queueMutex to safely modify the tasks queue
        unique_lock<mutex> lock(queueMutex);
        // Add the task to the queue
        tasks.emplace(task); 
    }
    // Notify one worker thread to process the task
    condition.notify_one();
}

// Worker thread function that continuously processes tasks from the task queue
void ThreadPool::workerThread() {
    while (true) {
        function<void()> task;
        {
            // Lock the queueMutex to safely access the task queue
            unique_lock<mutex> lock(queueMutex);

            // Wait until there is a task to process or the stop flag is set
            condition.wait(lock, [this] { return stop || !tasks.empty(); });

            // If stop is true and no tasks are left, exit the loop (worker thread terminates)
            if (stop && tasks.empty()) {
                return;
            }

            // Get the task from the front of the queue and remove it
            task = move(tasks.front()); 
            tasks.pop();
        }
        // Execute the task
        task();
    }
}
