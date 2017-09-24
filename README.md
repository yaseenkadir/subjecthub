# Subject Hub
Subject-Hub is a simple subject searching website to make it easier for university students to find
subjects they'd like to study.

This is a group project for Software Engineering Practice at the University of Technology Sydney.

|Branch|Status|
|------|------|
|Master|[![Build Status](https://travis-ci.org/yaseen95/subjecthub.svg?branch=master)](https://travis-ci.org/yaseen95/subjecthub)|

## Description
Subject Hub is designed to streamline the university subject-searching process. Typically, subject
documentation is verbose, and presents information extraneous to student concerns. Additionally,
relevant information is awkwardly distributed across several platforms, many of which are clunky,
unresponsive, and outdated. Subject Hub highlights only the most pertinent subject details, whilst
providing straightforward access to more specific information. Efficiency and responsiveness are
critical, given the importance of appropriate subject selection. Too often, students waste valuable
time navigating convoluted, buggy systems.

Many existing platforms rely on legacy technologies, making them inefficient and error-prone.
Subject Hub is designed modernly, and built using the latest technologies, ensuring security,
scalability, extensibility, and maintainability. This is especially crucial, given that
tech-proficient students are the target user base.

## Setup
### Angular
1. Follow angular install instructions.
2. `cd frontend`
3. `npm install` if you haven't installed yet. It may warn about fsevents. This is
safe to ignore.
4. `npm start` or `ng serve --open`
5. Press CTRL+C or CTRL+Q to exit

### Java / Spring
NOTE: Use `run-tests.sh` to run tests for both the backend and frontend.
#### Maven
##### Running App
1. `cd backend`
2. `mvn spring-boot:run`
3. Press CTRL+C or CTRL+Q to exit

##### Running tests
1. `mvn clean test -Ptest`

#### Intellij

##### Intellij Directory Setup
Before we start running we need to make sure that Intellij understands our package structure. It 
should figure it out by automatically, but just in case follow these instructions.

For each of the following directories make sure that they've been correctly marked. You can mark a
directory by right clicking on it and selecting `Mark Directory as` and choosing an option.

|Directory|Mark as|
|---------|-------|
|`backend/src/main/java`|Sources Root|
|`backend/src/main/resources`|Resources Root|
|`backend/src/test/java`|Test Sources Root|
|`backend/src/test/resources`|Test Resources Root|

If a directory has already been marked you can ignore it.

##### Running App
Navigate to `backend/src/main/java/com/example/subject/hub/` and right click on Application and 
select `Run Application`.

##### Running Tests
Because we have a different spring profile for tests we need to pass an argument so that spring 
knows which profile to use.

1. Navigate to `backend/src/test/`
2. Right click on `java` and select `Run All Tests`
3. If it doesn't fail you're all set up and everything is working fine. You can skip this section.
4. Otherwise, in intellij, on the top next to the "play" button there should be a dropdown saying
 "All in backend". Click on the dropdown and select "Edit Configurations"
5. In the dialog, next to "VM Options" add `-Dspring.profiles.active=test` after `-ea`. DON'T 
REMOVE `-ea`!
6. Press "OK"
7. Click on the play icon and run the tests. Everything should be working fine now.

## Backend
### Database
To connect to the embedded h2 database go to http://localhost:8080/h2-console/. In the JDBC url 
enter: jdbc:h2:mem:testdb, and then connect. No password is necessary.

The database is initialised with entries inside `backend/src/main/resources/data.sql`. If you wish
to add data, add it there.

#### Flyway
Flyway is used to manage database migrations. It is located in 
`backend/src/main/resources/db/migration/{platform}` where platform is the database driver.

The scripts in that file should only be used for schema migrations. If you wish to initialise the
database with entries, it should be done inside data.sql and not in the flyway directory.

## Deployment
Travis CI is used as the build server. If a master build passes all tests a Docker image is created
and uploaded to Dockerhub. Travis CI executes the `run-new-version.sh` script on the remote host.
That script must be uploaded by the user, it is NOT uploaded by Travis.

Both the docker image and the script need access to secrets which also must be uploaded to the
server at `~/.subject-hub/secrets.sh`. The secrets required are:

|Environment Secret|Description|
|---------|-------|
|`DOCKER_USERNAME`|dockerhub username with access to Subject Hub repo|
|`DOCKER_PASSWORD`|password for above dockerhub user|
|`KEYSTORE_PASSWORD`|password used to access ssl keystore (used to enable https)|
|`JWT_SECRET_KEY`|secret key used to sign jwts|
|`SUBJECT_HUB_DB_PASSWORD`|password to connect to subject-hub postgresql database|

We're storing JWT_SECRET_KEY as a variable instead of generating a random so JWTs are valid across
restarts.

### Database Setup
PostgreSQL is used to persist data. A postgres container is run on the same host as the application.
To setup the container:
```bash
docker pull postgres
docker run -d \
    --name subjecthubdb \
    -p 5432:5432 \
    -e POSTGRES_USER=springdbuser \
    -e POSTGRES_PASSWORD=${SUBJECT_HUB_DB_PASSWORD} \
    -e POSTGRES_DB=subjecthubdb \
    postgres
```
