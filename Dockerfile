FROM adoptopenjdk/openjdk12:latest
MAINTAINER arteemtkacheev@gmail.com
COPY telegram/build/libs/service-1.0.jar chat-manager.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "chat-manager.jar"]