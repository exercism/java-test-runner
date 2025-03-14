FROM bellsoft/liberica-runtime-container:jdk-21-crac-musl AS build

WORKDIR /app
COPY . /app
RUN /app/gradlew -i --stacktrace clean build

FROM bellsoft/liberica-runtime-container:jdk-21-crac-musl

WORKDIR /opt/test-runner
COPY bin/run-to-create-crac-checkpoint.sh bin/run-to-create-crac-checkpoint.sh
COPY bin/run-restore-from-checkpoint.sh bin/run-restore-from-checkpoint.sh
COPY --from=build /app/build/libs/java-test-runner.jar .

ENTRYPOINT ["sh", "/opt/test-runner/bin/run-to-create-crac-checkpoint.sh"]
