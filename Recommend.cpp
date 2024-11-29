#include "Recommend.h"
using namespace std;

// Constructor for the Recommend class
Recommend::Recommend() {}

// Finds all relevant users who have the specified movie in their watchlist
// and are not the user who initiated the recommendation process.
void Recommend::releventUsers() {
    // Open the file containing all users' IDs
    ifstream users_file("/usr/src/mytest/data/users.txt");
    if (!users_file.is_open()) {
    cout << "opening faild1" << endl;
    return;
    }

    string next_user;
    // Iterate through each user ID in the users file
    while (getline(users_file, next_user)) { 
        // Open the watchlist file for the current user
        ifstream next_user_watchlist("/usr/src/mytest/data/" + next_user + "_watchlist.txt");
        if (!users_file.is_open()) {
            cout << "opening faild2" << endl;
            return;
        }

        // Check if the movie is in the user's watchlist and the user is not the same as the one calling the recommendation
        if (isInFile(my_movie, next_user_watchlist) && my_user != next_user) {
            unsigned long userid = stoul(next_user);
            relevent_users.push_back(userid);
        }
        // Close the current user's watchlist file
        next_user_watchlist.close();
    }
    // Close the users file
    users_file.close();    
}

// Checks whether a specific string exists in the given file.
bool Recommend::isInFile(string str, ifstream& file) {
    if (!file.is_open()) {
        cout << "Error: File not open!" << endl;
        return false;
    }
    // Reset any error flags that may have occurred during file operations
    file.clear();
    // Move the read pointer to the beginning of the file
    file.seekg(0, ios::beg);

    string word;
    // Read the file line by line to check for the specified string
    while (getline(file, word)) {
        if (word == str){
            // The string is found in the file
            return true;
        }
    }
    // The string is not found
    return false;
}

// Calculates the weight of relevance for each relevant user by comparing their watchlists with the calling user's watchlist.
void Recommend::weightsOfRelevent() {
    // Calculate weights for each relevant user by comparing their watchlists
    for (int user : relevent_users) {
        // Compute the weight for the user
        int weight = calculateWeight(to_string(user));
        // Store the computed weight
        weights_relevent_users.push_back(weight);
    }
}

// Compares the watchlists of two users and calculates a weight based on the number of matching movies.
int Recommend::calculateWeight(string user_tocal) {
    // Initialize weight to 0
    int weight = 0;
    string movie_tocomp;

    // Open the calling user's watchlist file and the current user's watchlist file
    ifstream my_user_movies("/usr/src/mytest/data/" + my_user + "_watchlist.txt");
    ifstream user_tocal_movies("/usr/src/mytest/data/" + user_tocal + "_watchlist.txt");
    if (!my_user_movies.is_open() || !user_tocal_movies.is_open()) {
        cout << "opening faild3" << endl;
        return 1;
    }

    // Compare each movie in the calling user's watchlist with the current user's watchlist
    while (getline(my_user_movies, movie_tocomp)) { 
        if (isInFile(movie_tocomp, user_tocal_movies)){
            // Increment weight for each matching movie
            weight++;
        }
    }

    // Close both watchlist files
    my_user_movies.close();
    user_tocal_movies.close();

    return weight;
}

// Creates a map of movies recommended by relevant users, weighted by relevance scores.
void Recommend::makeMap() {
    // Open the calling user's watchlist file
    ifstream my_user_movies("/usr/src/mytest/data/" + my_user + "_watchlist.txt");
    
    // Iterate through relevant users and their watchlists
    for (int i = 0; i < relevent_users.size(); i++) {
        ifstream user_movies("/usr/src/mytest/data/" + to_string(relevent_users[i]) + "_watchlist.txt");
        string movie;

        // Add movies that are not in the calling user's watchlist and are not the movie that initiated the recommendation process to the map.
        while (getline(user_movies, movie)) { 
            if (!isInFile(movie, my_user_movies) && movie != my_movie){
                int movieid = stoul(movie);
                movies_weights[movieid] += weights_relevent_users[i];
            }
        }
        // Close the current user's watchlist file
        user_movies.close();
    }
    // Close the calling user's watchlist file
    my_user_movies.close();
}

// Validates the user input, ensuring it contains exactly two numeric values (user ID and movie ID).
bool Recommend::isInvalid(string input) {
    // Input must be at least 3 characters long
    if (input.size() < 3) {
        return false;
    }

    int pos = 0;
    // Skip leading spaces
    while (pos < input.size() && input[pos] == ' ') pos++;

    // Find the position of the first space after the first word (user ID).
    int nextPos = input.find(' ', pos);
    // Check if there is more then one word
    if (nextPos != string::npos) {
        // Extract the first word as the user ID.
        my_user = input.substr(pos, nextPos - pos);
        // Move position past the first word and space.
        pos = nextPos + 1;
    } else {
        // Return false if there is no second word
        return false;
    }

    // Skip spaces before the second word
    while (pos < input.size() && input[pos] == ' ') pos++;

    // Find the position of the next space after the second word.
    nextPos = input.find(' ', pos);
    if (nextPos != string::npos) {
        // Extract the second word as the movie ID.
        my_movie = input.substr(pos, nextPos - pos);
    } else {
        // If there are no more spaces, the rest of the string is the movie ID.
        my_movie = input.substr(pos);
    }

    // Check if there are additional words beyond the second one.
    pos = nextPos;
    while (pos < input.size() && input[pos] == ' ') pos++;
    if (pos < input.size()) {
        // If there are extra characters, the input is invalid.
        return false;
    }

    // Try to convert both user ID and movie ID to integers.
    try {
        unsigned long num1 = stoul(my_user);
        unsigned long num2 = stoul(my_movie);
    } catch (const invalid_argument& e) {
        // If conversion fails, the input is invalid.
        return false;
    } catch (const out_of_range& e) {
        // If the numbers are too large, the input is invalid.
        return false;
    }
    
    // If all checks pass, the input is valid.
    return true;
}

// Sorts movies by their weights in descending order.
vector<unsigned long> Recommend::sortMovies() {
    // Convert the movies_weights map to a vector of pairs
    vector<pair<unsigned long, int>> map_vec(movies_weights.begin(), movies_weights.end());
    
    // Sort the vector using a custom comparator
    sort(map_vec.begin(), map_vec.end(), [](pair<unsigned long, int>& a, pair<unsigned long, int>& b) {
        // If weights are equal, sort by movie ID
        if (a.second == b.second) {
            return a.first < b.first;
        }
        // Otherwise, sort by weight
        return a.second > b.second;
    });

    // Extract the movie IDs in sorted order
    vector<unsigned long> sortedMovies;
    for (auto& pair : map_vec) {
        sortedMovies.push_back(pair.first);
    }
    // Return the sorted movie IDs
    return sortedMovies;
}

// Finds relevant movie recommendations based on the user's preferences by identifying other users who have similar tastes in movies.
void Recommend::execute(string input) {
    // Validate the input
    if (!isInvalid(input)) {
        // Exit if the input is invalid
        return;
    }

    // Find relevant users
    releventUsers();
    
    // Calculate weights for relevant users
    weightsOfRelevent();

    // Build the map of movies and their weights
    makeMap();

    // Sort movies and display the top 10 recommendations
    vector<unsigned long> sortedMovies = sortMovies();
    for (int i = 0; i < 10 && i < sortedMovies.size(); i++) {
        cout << sortedMovies[i] << " ";
    }
}
