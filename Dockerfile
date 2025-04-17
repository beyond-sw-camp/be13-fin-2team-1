FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="village1031 <village1031@gmail.com>"
LABEL version="1.0"
COPY ./build/libs/Schocolla-api-0.0.1-SNAPSHOT.jar /root
ARG BUILD_PORT=8080
ENV TZ=Asia/Seoul
EXPOSE ${BUILD_PORT}
WORKDIR /root
CMD [ "java", "-jar", "Schocolla-api-0.0.1-SNAPSHOT.jar" ]