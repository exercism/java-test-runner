#!/usr/bin/env sh

# Synopsis:
# Create the JVM's ahead-of-time (AOT) cache for the test runner.
# This runs once, while the Docker image is built.

# Output:
# Writes /opt/test-runner/java-test-runner.aot, which bin/run.sh passes to the JVM.

# Without a cache, every run spends more than a second finding, loading and linking
# the classes of javac, JUnit and AssertJ, and running their code in the interpreter.
# This script runs the test runner once on the sample exercise in aot-training/.
# The JVM records every class loaded during that run and saves them, already loaded
# and linked, in the cache, along with profiling data for their methods.
# Every later run starts from that state.

# The JVM only uses the cache when a run uses the same jar and compatible options,
# so this must use the same options as bin/run.sh. If the JVM can't use the cache,
# it will print an error and run as before, without the speed-up.

set -e

training_folder="/tmp/aot-training"

rm -rf "${training_folder}"
cp -R /opt/test-runner/aot-training "${training_folder}"

java -XX:AOTCacheOutput=/opt/test-runner/java-test-runner.aot \
     -jar /opt/test-runner/java-test-runner.jar training "${training_folder}" "${training_folder}"

rm -rf "${training_folder}"
