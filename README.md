# Netflix-Project
the project is a reccomendation system for movies.
In the system we have data storage that contains users and the movies that they watched.
The project has 3 main function: 
1. help - will print the functions and the input format.
2. add - will add movies for each user (if user doesn't exist. create him).
3. reccomend - will print a recommendation according to the movie that inserted.

Run command
main:
cd src
docker build -t netflix .
docker run -it -v netflix:/usr/src/mytest/data netflix

test:
cd src
docker build -t netflix .
docker-compose up tests

image UML
https://github.com/user-attachments/assets/f4250997-4a3c-4acd-9eea-70773f111ed6


image 
https://github.com/user-attachments/assets/4a173824-a125-4265-933e-0dae46754e52
