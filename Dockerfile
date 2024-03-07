FROM openjdk:17-oracle
EXPOSE 8080
ADD target/college-api.jar college-api.jar
ENTRYPOINT ["java", "-jar", "college-api.jar"]
