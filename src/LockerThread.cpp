#include "LockerThread.h"
#include <mutex>

using namespace std;
mutex myLock;

void LockerThread::on()
{
    myLock.lock();
}

void LockerThread::off()
{
    myLock.unlock();
}

