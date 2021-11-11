# === Build builder image ===

FROM gradle:6.8.3-jdk11 AS build

WORKDIR /home/builder

# Prepare required project files
COPY lib/src ./src
COPY lib/build.gradle ./

# Build test runner
RUN echo "no-cache"
RUN gradle -i clean build
RUN gradle -i shadowJar \
    && cp build/libs/autotest-runner.jar .

FROM maven:3.8.3-jdk-11 AS cache

# Ensure exercise dependencies are downloaded
WORKDIR /opt/exercise
COPY exercise .
RUN mvn test dependency:go-offline -DexcludeReactor=false

# === Build runtime image ===

FROM maven:3.8.3-jdk-11
WORKDIR /opt/test-runner

# Copy binary and launcher script
COPY bin/ ./bin/
COPY --from=build /home/builder/autotest-runner.jar ./

# Copy cached dependencies
COPY --from=cache /root/.m2 /root/.m2

# Copy Maven pom.xml
COPY --from=cache /opt/exercise/pom.xml /home/pom.xml

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
