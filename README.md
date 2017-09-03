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

## Database
To connect to the embedded h2 database go to http://localhost:8080/h2-console/. In the JDBC url 
enter: jdbc:h2:mem:testdb, and then connect. No password is necessary.

The database is initialised with entries inside `backend/src/main/resources/data.sql`. If you wish
to add data, add it there.

### Flyway
Flyway is used to manage database migrations. It is located in 
`backend/src/main/resources/db/migration/{platform}` where platform is the database driver.

The scripts in that file should only be used for schema migrations. If you wish to initialise the
database with entries, it should be done inside data.sql and not in the flyway directory.
