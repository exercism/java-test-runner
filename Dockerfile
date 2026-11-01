FROM gradle:9.5.0-jdk25@sha256:03305b464e024b29cfaad1c4a41fed61d06d15453176d2180f65bd4358b789a6 AS build

WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle -i --stacktrace clean build

FROM eclipse-temurin:27_35-jdk@sha256:473d02d477a8152a05dc42fde10b965754f1863d45e586a97763ef298965c48b

WORKDIR /opt/test-runner
COPY bin/run.sh bin/run.sh
COPY --from=build /app/build/libs/java-test-runner.jar .

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
