#ifndef ILOCKER_H
#define ILOCKER_H

using namespace std;

// The Locker interface defines the structure for lockers.
class ILocker {
    public:
        // Pure virtual function 'on' for funcion "lock".
        virtual void on() = 0;
        // Pure virtual function 'off' for funcion "unlock".
        virtual void off() = 0;
    };
#endif