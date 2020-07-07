FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/YTDownloader-1.0.0.jar

COPY ${JAR_FILE} app.jar
EXPOSE 9090:9090
ENTRYPOINT ["java","-jar","app.jar"]