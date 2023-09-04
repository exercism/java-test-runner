# === Build builder image ===

FROM gradle:8.2-jdk17 AS build

WORKDIR /home/builder

# Prepare required project files
COPY lib/src ./src
COPY lib/build.gradle ./

# Build test runner
RUN gradle -i clean build
RUN gradle -i shadowJar \
    && cp build/libs/autotest-runner.jar .

# === Build runtime image ===

FROM eclipse-temurin:17-focal
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ ./bin/
COPY --from=build /home/builder/autotest-runner.jar ./

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
