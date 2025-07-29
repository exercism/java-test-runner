#!/usr/bin/env sh

# Synopsis:
# Build a Docker image containing a CraC checkpoint to restart from.
# An initial image is built. A container is created from that image
# and tests are run to warm up the JVM. Once the runner has finished running
# all the tests it creates a checkpoint of the VM process before exiting.
# This checkpoint is written to the host file system, so it can be copied
# into the final image.
# Then the final image is created, which restores from the checkpoint
# instead of starting a new JVM
# This avoids slow JVM start up and JVM warm up costs.
#
# Example:
# ./bin/build-crac-checkpoint-image.sh

create_checkpoint() {
    # Copy all tests into one merged project, so we can warm up the JVM
    # TODO(FAP): this is missing some tests as most tests use the same filenames
    mkdir -p tests/merged
    for dir in tests/*; do
        if [ -d "$dir" ] && [ "$dir" != "tests/merged/" ]; then
            rsync -a "$dir"/ tests/merged/
        fi
    done

    real_path() {
        echo "$(cd "$(dirname -- "$1")" >/dev/null; pwd -P)/$(basename -- "$1")";
    }

    mkdir -p build/cr

    image_tag="$1"
    slug="merged"
    solution_dir=$(realpath "tests/merged/")
    output_dir=$(realpath "tests/merged/")

    docker run --cap-add CHECKPOINT_RESTORE \
           --cap-add SYS_PTRACE \
           --name java-test-runner-crac \
           --network none \
           --mount type=bind,src="${solution_dir}",dst=/solution \
           --mount type=bind,src="${output_dir}",dst=/output \
           --mount type=bind,src="$(real_path build/cr)",dst=/opt/test-runner/crac-checkpoint \
           --mount type=tmpfs,dst=/tmp \
           "${image_tag}" "${slug}" /solution /output

    docker rm -f java-test-runner-crac
    rm -rf tests/merged/
}

# 1. Build jar outside of Docker, so we can copy the jar into both images
echo "build-crac-checkpoint-image.sh: Building jar with Gradle"
./gradlew build

image_tag="exercism/java-test-runner-crac-checkpoint"

# 2. Build first image with an entrypoint that will create a CraC checkpoint
echo "build-crac-checkpoint-image.sh: Building image that creates CraC checkpoint"
docker build -t "$image_tag" -f Dockerfile.createCheckpoint .

# 3. Run a container from image created in step 2 and create CraC checkpoint
echo "build-crac-checkpoint-image.sh: Creating CraC checkpoint"
create_checkpoint "$image_tag"

# Build final test runner image, includes the jar and the checkpoint created in step 1 and 3
echo "build-crac-checkpoint-image.sh: Building final test runner image"
docker build -t exercism/java-test-runner -f Dockerfile .
