FROM bellsoft/liberica-runtime-container:jdk-21-crac-musl AS build

WORKDIR /opt/test-runner
COPY build/libs/java-test-runner.jar /opt/test-runner/java-test-runner.jar
COPY bin/run-restore-from-checkpoint.sh bin/run-restore-from-checkpoint.sh
COPY --link build/cr /opt/test-runner/crac-checkpoint

ENTRYPOINT ["sh", "/opt/test-runner/bin/run-restore-from-checkpoint.sh"]
