#ifndef RECOMMEND_H
#define RECOMMEND_H

#include <string>
#include <fstream>
#include <iostream>
#include <vector> 
#include <map>
#include <algorithm>
#include "ICommand.h"
#include "dataFuncs.h"


using namespace std;

class Recommend : public ICommand {
    private:
        string my_user;
        string my_movie;

        // Finds all relevant users who have the specified movie in their watchlist.
        vector<string> releventUsers(); 
        // Calculates the weight of relevance for each relevant user by comparing their watchlists with the calling user's watchlist.
        vector<int> weightsOfRelevent(vector<string>& relevent_users); 
        // Compares the watchlists of two users and calculates a weight based on the number of matching movies.
        int calculateWeight(string user_tocal); 
        // Creates a map of movies recommended by relevant users, weighted by relevance scores.
        map<string, int> makeMap(vector<string>& relevent_users, vector<int>& weights_relevent_users);
        // Sorts movies by their weights in descending order.
        vector<string> sortMovies(map<string, int>& movies_weights); 
    public:
        // Validates the user input, ensuring it contains exactly two numeric values (user ID and movie ID).
        bool isInvalid(string input) override; 
        // Finds relevant movie recommendations based on the user's preferences by identifying other users who have similar tastes in movies.
        string execute(string input) override; 
};
#endif