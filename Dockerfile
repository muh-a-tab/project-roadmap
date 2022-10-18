FROM openjdk:11 as build
COPY target/roadmap-0.0.1-SNAPSHOT.jar roadmap.jar
ENTRYPOINT ["java","-jar","/roadmap.jar"]