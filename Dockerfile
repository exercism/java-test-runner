FROM gradle:8.12-jdk21 AS build

WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle -i --stacktrace clean build

FROM eclipse-temurin:21

WORKDIR /opt/test-runner
COPY bin/run.sh bin/run.sh
COPY --from=build /app/build/libs/java-test-runner.jar .

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
