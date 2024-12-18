#include "LockerThread.h"
#include <mutex>

using namespace std;
mutex myLock;

void Locker::on()
{
    myLock.lock();
}

void Locker::off()
{
    myLock.unlock();
}