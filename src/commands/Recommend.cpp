#include "Recommend.h"
#include "ICommand.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include "dataFuncs.h"
#include <string>


using namespace std;

// Finds all relevant users who have the specified movie in their watchlist
// and are not the user who initiated the recommendation process.
vector<string> Recommend::releventUsers() {
    vector<string> relevent_users;
        string next_user;
        vector<string> users_vec = dataToVec("users");

        for (int i = 0; i < users_vec.size(); i++) {
            next_user = users_vec[i];
            if (isInFile(my_movie, next_user+"_watchlist") && my_user != next_user) {
                relevent_users.push_back(next_user);
            }
        }
    return relevent_users;    
}

// Calculates the weight of relevance for each relevant user by comparing their watchlists with the calling user's watchlist.
vector<int> Recommend::weightsOfRelevent(vector<string>& relevent_users) {
    vector<int> weights_relevent_users;
    // Calculate weights for each relevant user by comparing their watchlists
    for (int i = 0; i < relevent_users.size(); i++) {
        // Compute the weight for the user
        int weight = calculateWeight(relevent_users[i]);
        // Store the computed weight
        weights_relevent_users.push_back(weight);
    }
    return weights_relevent_users;
}


// Compares the watchlists of two users and calculates a weight based on the number of matching movies.
int Recommend::calculateWeight(string user_tocal) {
    // Initialize weight to 0
    int weight = 0;
    string movie_tocomp;
    vector<string> my_user_movies = dataToVec(my_user+"_watchlist");

    for (int i = 0; i < my_user_movies.size(); i++) {
        movie_tocomp = my_user_movies[i];
        if (isInFile(movie_tocomp, user_tocal+"_watchlist")) {
            // Increment weight for each matching movie
            weight++;
        }
    }
    return weight;
}

// Creates a map of movies recommended by relevant users, weighted by relevance scores.
map<string, int> Recommend::makeMap(vector<string>& relevent_users, vector<int>& weights_relevent_users) {
    map<string, int> movies_weights;

    // Iterate through relevant users and their watchlists
    for (int i = 0; i < relevent_users.size(); i++) {
        string movie;

        // Add movies that are not in the calling user's watchlist and are not the movie that initiated the recommendation process to the map.
        vector<string> user_movies = dataToVec(relevent_users[i]+ "_watchlist");

        for (int j = 0; j < user_movies.size(); j++) {
            movie = user_movies[j];
            if (!isInFile(movie, my_user + "_watchlist") && movie != my_movie){
                movies_weights[movie] += weights_relevent_users[i];
            }
        }
    }
    return movies_weights;
}

// Validates the user input, ensuring it contains exactly two numeric values (user ID and movie ID).
bool Recommend::isInvalid(string input) {
    // If the input is less than 3 characters long, it's invalid.
    if (input.size() < 3) {
        return true;
    }
    // Convert the input string into a stringstream.
    stringstream ss(input);
    string word;
    int wordCounter = 0;
    // For each word in the input.
    while (ss >> word) {   
        wordCounter++; 
        // Check if every character in the word is a digit.
        // for (char ch : word) {
        //     //If any character is not a digit, return false.
        //     if (!isdigit(ch)) 
        //         return true;   
        //     if (!(stoi(input) > 0))
        //         return true;
        // }

        // // Attempt to convert the string `word` to an unsigned long.
        // try {
        //     unsigned long num1 = stoul(word);
        // } catch (const invalid_argument& e) {
        //     // If conversion fails, the input is invalid.
        //     return true;
        // } catch (const out_of_range& e) {
        //     // If the numbers are too large, the input is invalid.
        //     return true;
        // }
    }
    if (wordCounter != 2) {
        return true;
    }
    //The input is valid.
    return false;
}


// Sorts movies by their weights in descending order.
vector<string> Recommend::sortMovies(map<string, int>& movies_weights) {
    // Convert the movies_weights map to a vector of pairs
    vector<pair<string, int>> map_vec(movies_weights.begin(), movies_weights.end());
    
    // Sort the vector using a custom comparator
    sort(map_vec.begin(), map_vec.end(), [](pair<string, int>& a, pair<string, int>& b) {
        // If weights are equal, sort by movie ID
        if (a.second == b.second) {
            return stoul(a.first) < stoul(b.first);
        }
        // Otherwise, sort by weight
        return a.second > b.second;
    });

    // Extract the movie IDs in sorted order
    vector<string> sortedMovies;
    for (auto& pair : map_vec) {
        sortedMovies.push_back(pair.first);
    }
    // Return the sorted movie IDs
    return sortedMovies;
}

// Finds relevant movie recommendations based on the user's preferences by identifying other users who have similar tastes in movies.
string Recommend::execute(string input) {
    string response = "";
    // Validate the input
    if (isInvalid(input)) {
        response += "400 Bad Request";
        // Exit if the input is invalid
        return response;
    }
    // init the fields
    string word;
    stringstream ss(input);
    ss >> word;
    my_user = word;
    ss >> word;
    my_movie = word;

    if (!isInFile(my_user, "users")) {
        response += "404 Not Found";
        // Exit if user not found
        return response;
    }
    

    
    // Find relevant users
    vector<string> relevent_users = releventUsers();
    if(relevent_users.empty()) {
        if(isInFile(my_movie, my_user + "_watchlist")) {
            response += "200 Ok\n\n";
            return response;
        }
        response += "404 Not Found";
        // Exit if user not found
        return response;
    }

    response += "200 Ok\n\n";
    // Calculate weights for relevant users
    vector<int> weights_relevent_users = weightsOfRelevent(relevent_users);

    // Build the map of movies and their weights
    map<string, int> movies_weights = makeMap(relevent_users, weights_relevent_users);

    // Sort movies and display the top 10 recommendations
    vector<string> sortedMovies = sortMovies(movies_weights);

    // Return the recommended movies
    for (int i = 0; i < 10 && i < sortedMovies.size(); i++) {
        response += sortedMovies[i] + " ";
    }
    return response;
}