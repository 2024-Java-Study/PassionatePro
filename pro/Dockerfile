FROM amazoncorretto:17
ARG JAR_FILE=build/libs/pro-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080