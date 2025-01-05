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
    ThreadPool(size_t numThreads);
    ~ThreadPool();

    void submit(std::function<void()> task);

private:
    std::vector<std::thread> workers;
    std::queue<function<void()>> tasks;        
    std::mutex queueMutex;                         
    std::condition_variable condition;            
    std::atomic<bool> stop;       

    void workerThread(); 
};

#endif 