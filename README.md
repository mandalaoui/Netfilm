# Netflix Client-Server

# Table of Contents

-	Overview
-	Project structure
-	UML
-	Command project
-	Change in project
-	Running
-	Running Examples

# Overview

The Project implements a client-server based movie recommendation system. The client implement in python and provides a user interface for sending commands and display result, the server implement in c++ and will handle all the business logic.

# Project Structure
Server:

-	Handle multiple clients concurrently using a thread per client.
-	Manages the information and performs all the logical calculations.
-	Processes commands, and sends back the result.
    
Client:

-	Connect to server on TCP.
-	calls a command from the user.
-	Send user's command.
-	Display server response.
# UML
<img width="938" alt="image" src="https://github.com/user-attachments/assets/1561690d-be7b-47c4-95ea-1a223902ca2b" />

# Command project
1. help - will print the functions and the input format.
2. GET - will print a recommendation according to the movie that inserted.
3. POST - will add user and movies to the user (if user doesn't exist. create him).
4. PATCH -  will add movies to existing user.
5. DELETE - will delete movies from existing user.

All the commands return the relevant output by the server.
The client recives the result and prints it.

# Changes in project

## Question 

1.	Did the fact that the command names changed require you to touch code that should be "closed for changes but open for extension"?
   
    -	No, in class MapCommands – we just changed the name of the pointer' and the input that required to call the relevant class "GET".

2.	Did the fact that new commands were added require you to touch code that should be "closed to changes but open to extension"?
   
    -	No, we create class "Map Commands" that contain map of functions object so that each new command can be added to the map.
  
    -	In addition, to prevent code repetition, we created new classes that inherit from the previous and the relevant change has been made in each one.
  
3.	Did the fact that the command output changed require you to touch code that should be "closed to changes but open to extension"?
   
    -	Yes, We needed to change the value that returned from the function "execute" of each command from void to string. For each command we returned the relevant output.

4.	Did the fact that the input/output comes from sockets instead of the console require you to touch code that should be "closed to changes but open to extension"?
   
    -	Previously, we printed the command results directly to the screen.
    -	Now, the server processes all commands using clientHandle, which connects the client to the appropriate socket.

## Change previous classes
In order to follow the SOLID principle;
We created file with function called "dataFuncs",  that includes functions for writing to file and reading from file (checking if specific content exists in the file).
We updated the command classes accordingly.

## Updates
1. Implemented a threaded multi-client server in C++ that simultaneously handles several clients.
2. Implemented a client in Python.
3. Created Docker containers for the server and the client and updated the Docker Compose configuration accordingly.


# Running

Server:

Build: docker-compose build

Run: docker-compose run -it -v netflix:/usr/src/mytest/data --rm --name netflix-project server 8080

*Prepare for next run: docker-compose down --remove-orphans

Client:

Run: docker-compose run --rm client netflix-project 8080

Tests:

Build: docker-compose build
Run: docker-compose up tests



