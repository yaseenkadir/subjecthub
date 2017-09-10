FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD backend/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8443
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=prod \
    -Dserver.ssl.key-store-password=$KEYSTORE_PASSWORD \
    -jar /app.jar
