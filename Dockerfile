# === Build builder image ===

FROM gradle:6.8.3-jdk11 AS build

WORKDIR /home/builder

# Prepare required project files
COPY lib/src ./src
COPY lib/build.gradle ./

# Build test runner and copy cached dependencies
RUN gradle --no-daemon -i shadowJar \
    && cp build/libs/autotest-runner.jar . \
    && mkdir -p /opt/test-runner/gradle \
    && cp -r .gradle/ /opt/test-runner/gradle/.gradle/ \
    && unlink /root/.gradle \
    && ln -s /opt/test-runner/gradle/.gradle /root/.gradle

# === Build runtime image ===

FROM gradle:6.8.3-jdk11
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ bin/
COPY --from=build /home/builder/autotest-runner.jar .
ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
