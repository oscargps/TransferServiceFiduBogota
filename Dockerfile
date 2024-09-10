FROM gradle:7.5.1-jdk17 AS build

WORKDIR /app

COPY . .

RUN gradle build -x test --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/TransferService-*.jar /app/TransferService.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/TransferService.jar"]
