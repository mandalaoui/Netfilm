## Running the Application

### Download node_modules

### Create .env.local:
- Create config folder at folder "api"
- In config create a file ".env.local"
- In .env.local insert the code:
```bash
CONNECTION_STRING=mongodb://host.docker.internal:27017/api
PORT=12345
RECCOMENDATION_IP=netflix-project
RECCOMENDATION_PORT=8080
JWT_KEY=secretkey441311
```

### Build:
in the main file:
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

![הרצת מערכת](https://github.com/user-attachments/assets/daa62e85-26e3-4fbf-955b-42e835eb0cca)


### Android Client:
Create a new device (Emulator) - pixel 2, R
Sync
Run the Emulator

![הרצת אנדוראיד](https://github.com/user-attachments/assets/7009ead4-cb20-42ce-84b0-d4c029bd14b0)


## Additional Notes
### Admin Panel Access:
The admin user is manually defined in MongoDB.
Upon re-login, the admin can access the management dashboard.
All files are stored inside Docker containers.
