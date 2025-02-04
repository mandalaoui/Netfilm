## Table of Contents

- Overview
- Project Structure
- API Design
- Client Features

## Overview

The project consists of a client-server-based system for movie recommendations and streaming.  
The previous stage focused on implementing a RESTful API, handling user authentication, storing data in MongoDB, and integrating a recommendation system.

In this phase, a React-based web client was developed to interact with the API, allowing users to browse movies, watch trailers, search, and manage content. Additionally, a theme toggle (Dark/Light mode) was implemented.

## Project Structure

### Server:
- Exposes a RESTful API to manage users, movies, categories, and recommendations.
- Stores user and movie data in MongoDB.
- Handles authentication and session management via JWT.
- Manages API requests over HTTP using a multi-threaded approach with ThreadPool.

### Client (React):
Communicates with the server via HTTP requests using fetch().
Provides UI for authentication, movie browsing, search, and viewing recommendations.
Implements a Dark/Light mode toggle stored in localStorage.

### Client (Android):
Communicates with the server via HTTP requests using RetroFit.
Provides UI for authentication, movie browsing, search, and viewing recommendations.
Implements a Dark/Light mode toggle stored in localStorage.


## API Design

### User Authentication and Registration:
- **POST** `/api/users` - Creates a new user with provided details (name, profile picture, etc.).
- **GET** `/api/users/:id` - Retrieves user details.
- **POST** `/api/tokens` - Authenticates a user and returns a JWT token.

> Note: The user ID is passed in the request header for authenticated API calls.

### Home Page (Movies):
Once logged in, users see a homepage displaying available movies.

### Categories:
- **GET** `/api/categories` - Retrieves a list of all categories.
- **POST** `/api/categories` - Creates a new category.
- **GET** `/api/categories/:id` - Retrieves category details.
- **PATCH** `/api/categories/:id` - Updates a category.
- **DELETE** `/api/categories/:id` - Deletes a category.

### Movies:
- **GET** `/api/movies` - Returns movies based on categories.
- **POST** `/api/movies` - Creates a new movie.
- **GET** `/api/movies/:id` - Retrieves details of a movie.
- **PUT** `/api/movies/:id` - Updates a movie.
- **DELETE** `/api/movies/:id` - Deletes a movie.
- **GET** `/api/movies/allmovies` - Returns all movies.

### Movie Recommendations:
- **GET** `/api/movies/:id/recommend` - Fetches recommended movies based on the given movie ID.
- **POST** `/api/movies/:id/recommend` - Adds the movie to the user's watched list.

> Note: The recommendation system interacts with the server via a socket connection.

### Search Functionality:
- **GET** `/api/movies/search/:query` - Returns movies matching the search query.

## Client Features

### Implemented Screens:
- **Landing Page** (for non-logged-in users)
- **Login Page**
- **Registration Page** (similar to login but with additional fields like name and profile picture)
- **Main Dashboard** (displays movies available for viewing)
- **Movie Details Page** (shows full movie details and recommended movies)
- **Movie Player Page** (video player for watching movies)
- **Search Results Page**
- **Admin Panel** (available only for admin users, configured manually in MongoDB)

### Authorization & Access Control:
- Everyone can access the landing page and the login/register pages.
- Registered Users can access the main dashboard, movie details, movie player, and search results.
- Admins can access the admin panel to manage movies and categories.

### Admin Panel Features:
- Add/Edit/Delete movies.
- Add/Edit/Delete categories.

### Theme Toggle (Dark/Light Mode):
- Implemented via `localStorage` to persist user preferences.
- Allows users to switch between themes via a button in the navigation bar.

![צפייה בקטגוריה ספציפית](https://github.com/user-attachments/assets/828fe58e-188d-4653-8ef1-6defd6530de0)

