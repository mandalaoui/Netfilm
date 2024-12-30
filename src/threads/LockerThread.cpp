#include "LockerThread.h"
#include <mutex>

using namespace std;
mutex myLock;

// Function 'on' for funcion "lock".
void LockerThread::on()
{
    myLock.lock();
}

// Function 'off' for funcion "unlock".
void LockerThread::off()
{
    myLock.unlock();
}

