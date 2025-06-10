FROM openjdk:21-jdk
ARG JAR_FILE=target/tech-challenge-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} tech-challenge-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/tech-challenge-0.0.1-SNAPSHOT.jar"]