# === Build builder image ===

FROM gradle:7.1-jdk11 AS build

WORKDIR /home/builder

# Prepare required project files
COPY lib/src ./src
COPY lib/build.gradle ./

# Build test runner
RUN gradle -i clean build
RUN gradle -i shadowJar \
    && cp build/libs/autotest-runner.jar .

# Ensure exercise dependencies are downloaded
WORKDIR /opt/exercise
COPY exercise .
RUN gradle build

# === Build runtime image ===

FROM gradle:7.1-jdk11
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ ./bin/
COPY --from=build /home/builder/autotest-runner.jar ./

# Copy cached dependencies
COPY --from=build /home/gradle /home/gradle

RUN pkill -f '.*GradleDaemon.*'
RUN rm -rf ~/.gradle/daemon

ENV GRADLE_OPTS="--add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.invoke=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.prefs/java.util.prefs=ALL-UNNAMED --add-opens java.prefs/java.util.prefs=ALL-UNNAMED --add-opens java.base/java.nio.charset=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED --add-opens java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xms256m -Xmx2048M -Dfile.encoding=UTF-8 -Duser.country=US -Duser.language=en -Duser.variant"

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
