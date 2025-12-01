FROM docker.io/library/gradle:9.2.1-jdk25 AS build

WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle -i --stacktrace clean build

FROM docker.io/library/eclipse-temurin:25.0.1_8-jdk

WORKDIR /opt/test-runner
COPY bin/run.sh bin/run.sh
COPY --from=build /app/build/libs/java-test-runner.jar .

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
