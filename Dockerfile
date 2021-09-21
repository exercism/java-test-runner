# === Build builder image ===

FROM gradle:6.8.3-jdk11 AS build

WORKDIR /home/builder

# Prepare required project files
COPY lib/src ./src
COPY lib/build.gradle ./

# Build test runner
RUN gradle --no-daemon -i shadowJar \
    && cp build/libs/autotest-runner.jar .

# === Build runtime image ===

FROM gradle:6.8.3-jdk11
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ bin/
COPY --from=build /home/builder/autotest-runner.jar .

# Copy cached dependencies
RUN mkdir -p /opt/test-runner/gradle
COPY --from=build /home/builder/.gradle /opt/test-runner/gradle
RUN unlink /root/.gradle \
    && ln -s /opt/test-runner/gradle/.gradle /root/.gradle

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
