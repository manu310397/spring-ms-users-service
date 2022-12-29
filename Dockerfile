FROM amazoncorretto:11.0.15-alpine
VOLUME /tmp
COPY target/spring-ms-users-service-0.0.1-SNAPSHOT.jar spring-ms-users-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/spring-ms-users-service.jar"]