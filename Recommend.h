#include <string>
#include <fstream>
#include <iostream>
#include <vector> 
#include <map>

using namespace std;

class Recommend : public ICommand {
    private:
    string my_user;
    string my_movie;
    vector<int> relevent_users;
    vector<int> weights_relevent_users;
    map<int, int> movies_weights;

    void releventUsers();
    bool isInFile(string str, ifstream file);
    void weightsOfRelevent();
    int calculateWeight(string user_tocal);
    void makeMap();
    bool isInvalid(string input);
    vector<int> sortMovies();

    public:
    Recommend();
    void execute(string input);
    void main();
}