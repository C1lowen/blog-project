FROM openjdk:17

WORKDIR /app

COPY target/Users-0.0.1-SNAPSHOT.jar Users-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "Users-0.0.1-SNAPSHOT.jar"]
