#include "Recommend.h"
using namespace std;

class Recommend : public ICommand {
    string my_user;
    string my_movie;
    vector<int> relevent_users;
    vector<int> weights_relevent_users;
    map<int, int> movies_weights;

    void releventUsers() {
        //open all users file
        ifstream users_file("users.txt");
        if (!users_file.is_open()) {
        cout << "opening faild" << endl;
        return 1;
        }
        string next_user;
        while (getline(users_file, next_user)) { 
            ifstream next_user_watchlist(next_user+"_watchlist.txt");
            //check if next user is relevan and not the user how call the recommend func
            if (isInFile(my_movie, next_user_watchlist) && my_user != next_user) {
                relevent_users.push_back(stoul(next_user));
            }
            next_user_watchlist.close();
        }
        users_file.close();    
    }

    //returns if the string is in the file
    bool isInFile(string str, ifstream file) {
        string word;
        while (getline(file, word)) { 
            if (word == str){
                return true;
            }
        }
        users_file.close();
        return false;
    }

    void weightsOfRelevent() {
        for (int user : relevent_users) {
            int weight = calculateWeight(user, to_string(user));
            movies_weights.push_back(weight);
        }
    }

    int calculateWeight(string user_tocal) {
        int weight = 0;
        string movie_tocomp;
        ifstream my_user_movies(my_user+"_watchlist.txt");
        ifstream user_tocal_movies(user_tocal+"_watchlist.txt");
        if (!my_user_movies.is_open() || !user_tocal_movies.is_open()) {
            cout << "opening faild" << endl;
            return 1;
        }
        while (getline(my_user_movies, movie_tocomp)) { 
            if (isInFile(movie_tocomp, user_tocal_movies)){
                weight++;
            }
        }
        my_user_movies.close();
        user_tocal_movies.close();
        return weight;
    }

    void makeMap() {
        for (int i = 0; i < relevent_users.size(); i++) {
            ifstream user_movies(relevent_users[i]+"_watchlist.txt");
            string movie;
            while (getline(user_movies, movie)) { 
                int movieid = stoul(movie);
                movies_weights[movieid] += weights_relevent_users[i];
            }
            user_movies.close();
        }
    }

    bool isInvalid(string input) {
        if (input.size() < 3) {
            return false;
        }
        int pos = 0;

        while (pos < input.size() && input[pos] == ' ') pos++;

        size_t nextPos = input.find(' ', pos);
        if (nextPos != string::npos) {
            my_user = input.substr(pos, nextPos - pos); // חיתוך המילה הראשונה
            pos = nextPos + 1; // עדכון המיקום
        } else {
            return false;
        }

        // דילוג על רווחים לפני המילה השנייה
        while (pos < input.size() && input[pos] == ' ') pos++;

        // מציאת המילה השנייה
        nextPos = input.find(' ', pos);
        if (nextPos != string::npos) {
            my_movie = input.substr(pos, nextPos - pos);
        } else {
            my_movie = input.substr(pos); // חיתוך המילה השנייה עד סוף המחרוזת
        }

        // בדיקה אם יש יותר משתי מילים
        pos = nextPos;
        while (pos < input.size() && input[pos] == ' ') pos++;
        if (pos < input.size()) {
            return false;
        }

        // נסיון להמיר את המילים למספרים
        try {
            int num1 = stoul(my_user);
            int num2 = stoul(my_movie);

        } catch () {
            return false;
        }
        return true;
    }

    vector<int> sortMovies() {
        vector<pair<int, int>> map_vec(movies_weights.begin(), movies_weights.end());
        // מיון הוקטור לפי values, ובמקרה של שוויון לפי keys
        sort(map_vec.begin(), map_vec.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
            if (a.second == b.second) {
                return a.first < b.first; // אם ה-values שווים, מיין לפי keys
            }
            return a.second < b.second; // אחרת מיין לפי values
        });

        // וקטור להחזקת ה-keys בסדר ממוין
        vector<int> sortedMovies;
        for (const auto& pair : map_vec) {
            sortedMovies.push_back(pair.first);
        }
        return sortedMovies;
    }

    Recommend(){}

    void execute(string input) {
        if (!isInvalid(input)) {
            return;
        }
        releventUsers();
        weightsOfRelevent();
        makeMap();
        vector<int> sortedMovies = sortMovies();
        for (int i = 0; i < 10; i++) {
            cout << sortedMovies[i] << " ";
        }
    }

    void main(){
        execute("15 102")
    }
}