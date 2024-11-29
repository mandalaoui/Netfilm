#ifndef RECOMMEND_H
#define RECOMMEND_H

#include <string>
#include <fstream>
#include <iostream>
#include <vector> 
#include <map>
#include <algorithm>
// #include "Icommand.cpp"
// #include "ICommand.h"

using namespace std;

// class Recommend : public Icommand {
class Recommend {
    private:
    string my_user;
    string my_movie;
    vector<unsigned long> relevent_users;
    vector<int> weights_relevent_users;
    map<unsigned long, int> movies_weights;

    public:
    Recommend(); // Constructor for the Recommend class
    void releventUsers(); // Finds all relevant users who have the specified movie in their watchlist
    bool isInFile(string str, ifstream& file); // Checks whether a specific string exists in the given file.
    void weightsOfRelevent(); // Calculates the weight of relevance for each relevant user by comparing their watchlists with the calling user's watchlist.
    int calculateWeight(string user_tocal); // Compares the watchlists of two users and calculates a weight based on the number of matching movies.
    void makeMap(); // Creates a map of movies recommended by relevant users, weighted by relevance scores.
    bool isInvalid(string input); // Validates the user input, ensuring it contains exactly two numeric values (user ID and movie ID).
    vector<unsigned long> sortMovies(); // Sorts movies by their weights in descending order.
    void execute(string input); // Finds relevant movie recommendations based on the user's preferences by identifying other users who have similar tastes in movies.
};

#endif