#!/usr/bin/env sh

# Synopsis:
# Run the test runner on a solution using the test runner Docker image.
# The test runner Docker image is built automatically.

# Arguments:
# $1: exercise slug
# $2: path to solution folder
# $3: path to output directory

# Output:
# Writes the test results to a results.json file in the passed-in output directory.
# The test results are formatted according to the specifications at https://github.com/exercism/docs/blob/main/building/tooling/test-runners/interface.md

# Example:
# ./bin/run-in-docker.sh two-fer path/to/solution/folder/ path/to/output/directory/

# If any required arguments is missing, print the usage and exit
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
    echo "usage: ./bin/run-in-docker.sh exercise-slug path/to/solution/folder/ path/to/output/directory/"
    exit 1
fi

slug="$1"
solution_dir=$(realpath "${2%/}")
output_dir=$(realpath "${3%/}")

# Create the output directory if it doesn't exist
mkdir -p "${output_dir}"

# Build the Docker image
bin/build-crac-checkpoint-image.sh

# Run the Docker image using the settings mimicking the production environment
docker run \
    --rm \
    --network none \
    --read-only \
    --mount type=bind,src="${solution_dir}",dst=/solution \
    --mount type=bind,src="${output_dir}",dst=/output \
    --mount type=tmpfs,dst=/tmp \
    exercism/java-test-runner "${slug}" /solution /output
