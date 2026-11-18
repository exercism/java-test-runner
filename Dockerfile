FROM gradle:9.5.0-jdk25@sha256:03305b464e024b29cfaad1c4a41fed61d06d15453176d2180f65bd4358b789a6 AS build

WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle -i --stacktrace clean build

FROM ghcr.io/graalvm/native-image-community:25.0.2@sha256:0d936f32bb8acb5bc60c41b33e05f064d7a6aaf36b726538296c54949bd4a3c0 AS native-javac

COPY --from=build /app/build/libs/java-test-runner.jar /tmp/java-test-runner.jar
COPY aot-training /tmp/aot-training
COPY bin/build-native-javac.sh /tmp/build-native-javac.sh
RUN sh /tmp/build-native-javac.sh

FROM eclipse-temurin:27_35-jdk@sha256:473d02d477a8152a05dc42fde10b965754f1863d45e586a97763ef298965c48b

WORKDIR /opt/test-runner
COPY bin/run.sh bin/run.sh
COPY --from=build /app/build/libs/java-test-runner.jar .
COPY bin/create-aot-cache.sh bin/create-aot-cache.sh
COPY aot-training aot-training
RUN sh bin/create-aot-cache.sh

# The native javac is added after the AOT cache is created. That way the training run
# compiles inside the JVM, and the cache holds javac's classes for runs that fall back to it.
COPY --from=native-javac /native-javac native-javac

ENTRYPOINT ["sh", "/opt/test-runner/bin/run.sh"]
