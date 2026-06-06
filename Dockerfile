FROM gradle:9.5.0-jdk25@sha256:03305b464e024b29cfaad1c4a41fed61d06d15453176d2180f65bd4358b789a6 AS build

WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle -i --stacktrace clean build

FROM eclipse-temurin:26.0.1_8-jdk@sha256:e1ccbf158a2818db7f770d0159f30a191e512c6a482797c55f7ae04a54247563

WORKDIR /opt/test-runner
COPY bin/run.sh bin/run.sh
COPY --from=build /app/build/libs/java-test-runner.jar .

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
