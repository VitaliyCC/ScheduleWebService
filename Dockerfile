FROM openjdk:11
ADD /target/webService-0.0.1-SNAPSHOT.war webservice.war
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "webservice.war"]
