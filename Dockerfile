FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY testng.xml master.xml signin_test_suite.xml ./
RUN mvn -B -DskipTests clean package

FROM selenium/standalone-chrome:4.27.0
USER root
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
WORKDIR /automation
COPY --from=build /app /automation
ENV EXECUTION_ENV=remote
ENV BROWSER=chrome
ENV HEADLESS=true
CMD ["mvn", "test", "-DsuiteFile=testng.xml", "-Dheadless=true", "-Dexecution_env=remote"]
