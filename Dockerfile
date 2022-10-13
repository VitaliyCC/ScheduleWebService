FROM openjdk:11
ADD /target/webService-0.0.1-SNAPSHOT.war webservice.war
COPY src/main/resources src/main/resources
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "webservice.war"]
