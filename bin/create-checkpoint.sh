#!/usr/bin/env sh

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

image_tag="${1:-exercism/java-test-runner-crac-checkpoint}"
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
