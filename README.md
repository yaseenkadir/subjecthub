# Subject Hub

## Setup
### Angular
1. Follow angular install instructions.
2. `cd frontend`
3. `npm install` if you haven't installed yet. It may warn about fsevents. This is
safe to ignore.
4. `npm start` or `ng serve --open`
5. Press CTRL+C or CTRL+Q to exit

### Java / Spring
#### Running Spring Boot App
1. `cd backend`
2. `mvn spring-boot:run`
3. Press CTRL+C or CTRL+Q to exit


## Other
To connect to the embedded h2 database go to http://localhost:8080/h2-console/. In the JDBC url 
enter: jdbc:h2:mem:testdb, and then connect. No password is necessary.
