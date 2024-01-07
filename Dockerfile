FROM openjdk:11
COPY target/Employee-Management-System-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
