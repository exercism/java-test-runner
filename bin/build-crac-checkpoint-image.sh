#!/usr/bin/env sh

# Synopsis:
# Build a Docker image containing a CraC checkpoint to restart from.
# An initial image is built. A container is created from that image
# and tests are run to warm up the JVM. Then a checkpoint is created.
# The final image is created by committing the containiner
# containing the checkpoint.

# build outside of Docker container, so we can copy jar into both images
./gradlew build

docker build -t exercism/java-test-runner-crac-checkpoint -f Dockerfile.createCheckpoint .

bin/create-checkpoint.sh

docker build -t exercism/java-test-runner -f Dockerfile .
