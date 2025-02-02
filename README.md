# Netflix Web Server

## Table of Contents

- Overview
- Project Structure
- API Design
- React Web Client Features
- Running the Application
- Additional Notes
- Running Examples

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
- Communicates with the server via HTTP requests using `fetch()`.
- Provides UI for authentication, movie browsing, search, and viewing recommendations.
- Implements a Dark/Light mode toggle stored in `localStorage`.
- Uses a simple global state without Redux/Context API.

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

### Movie Recommendations:
- **GET** `/api/movies/:id/recommend` - Fetches recommended movies based on the given movie ID.
- **POST** `/api/movies/:id/recommend` - Adds the movie to the user's watched list.

> Note: The recommendation system interacts with the server via a socket connection.

### Search Functionality:
- **GET** `/api/movies/search/:query` - Returns movies matching the search query.

## React Web Client Features

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
- A movie cannot exist without a category—if removed, it is automatically assigned to a default unAttached category and will not be displayed to users.

### Theme Toggle (Dark/Light Mode):
- Implemented via `localStorage` to persist user preferences.
- Allows users to switch between themes via a button in the navigation bar.

## Running the Application

### Build:
```bash
docker-compose build
```

### Prepare for next run (if needed):
```bash
docker-compose down --remove-orphans
```

## Running the Containers

### Recommendation System Server:
```bash
docker-compose run -it -v netflix:/usr/src/mytest/data --rm --name netflix-project server 8080
```

### API Server:
```bash
docker-compose run -it --rm --name app -p 12345:12345 -v mongo_data:/usr/src/data api
```

### React Web Client:
```bash
docker-compose run -it --rm --name netflix-web -p 3000:3000 web
```

> After starting the web client, the application is accessible at:
http://localhost:3000/

## Additional Notes
### Admin Panel Access:
The admin user is manually defined in MongoDB. 
Upon re-login, the admin can access the management dashboard.
All files are stored inside Docker containers.

### Running API Requests
A api_request.rest file is included for testing API calls. 

Usage steps:

- Install the REST Client extension in VS Code.
- Open api_request.rest.
- Click Send Request on the desired API operation.
- Replace placeholders (e.g., user IDs) as needed.

> This README reflects all changes introduced in Phase 4 of the project

## Running Examples
### 
