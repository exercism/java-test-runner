# === Build builder image ===

FROM gradle:6.8.3-jdk11 AS build

WORKDIR /home/builder

# Prepare required project files
COPY src ./src
COPY build.gradle ./

# Build test runner and copy cached dependencies
RUN gradle --no-daemon -i shadowJar \
    && cp build/libs/autotest-runner.jar . \
    && mkdir -p /opt/test-runner/gradle \
    && cp -r .gradle/ /opt/test-runner/gradle/.gradle/ \
    && unlink /root/.gradle \
    && ln -s /opt/test-runner/gradle/.gradle /root/.gradle

# === Build runtime image ===

FROM gradle:6.8.3-jdk11
ARG WORKDIR="/opt/test-runner"

# Copy binary and launcher script
COPY bin/ ${WORKDIR}/bin/
COPY --from=build /home/builder/autotest-runner.jar ${WORKDIR}

WORKDIR ${WORKDIR}
ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
