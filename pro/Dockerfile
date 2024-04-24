FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/pro-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./app.jar"]