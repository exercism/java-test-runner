#!/usr/bin/env sh

# Synopsis:
# Build a Docker image containing a CraC checkpoint to restart from.
# An initial image is built. A container is created from that image
# and tests are run to warm up the JVM. Then a checkpoint is created.
# The final image is created by committing the containiner
# containing the checkpoint.

docker build -t exercism/java-test-runner-crac-checkpoint .

# Copy all tests into one merged project, so we can warm up the JVM
# TODO(FAP): this is missing some tests as most tests use the same filenames
mkdir -p tests/merged
for dir in tests/*; do
    if [ -d "$dir" ] && [ "$dir" != "tests/merged/" ]; then
        rsync -a "$dir"/ tests/merged/
    fi
done

slug="merged"
solution_dir=$(realpath "tests/merged/")
output_dir=$(realpath "tests/merged/")

docker run --cap-add CHECKPOINT_RESTORE \
           --cap-add SYS_PTRACE \
           --name java-test-runner-crac \
           --network none \
           --mount type=bind,src="${solution_dir}",dst=/solution \
           --mount type=bind,src="${output_dir}",dst=/output \
           --mount type=tmpfs,dst=/tmp \
           exercism/java-test-runner-crac-checkpoint "${slug}" /solution /output

docker commit --change='ENTRYPOINT ["sh", "/opt/test-runner/bin/run-restore-from-checkpoint.sh"]' java-test-runner-crac exercism/java-test-runner-crac-restore

docker rm -f java-test-runner-crac
rm -rf tests/merged/
