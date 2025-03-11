#!/usr/bin/env bash

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
# ./bin/run-create-crac-checkpoint.sh two-fer path/to/solution/folder/ path/to/output/directory/

if [ $# -lt 3 ]
then
    echo "Usage:"
    echo "./bin/run-create-crac-checkpoint.sh two-fer ~/input/ ~/output/"
    exit 1
fi

problem_slug="$1"
input_folder="$2"
output_folder="$3"
tmp_folder="/tmp/solution"

mkdir -p $output_folder

rm -rf $tmp_folder
mkdir -p $tmp_folder

cd $tmp_folder
cp -R $input_folder/* .

find . -mindepth 1 -type f | grep 'Test.java' | xargs -I file sed -i "s/@Ignore(.*)//g;s/@Ignore//g;s/@Disabled(.*)//g;s/@Disabled//g;" file

# -XX:-UsePerfData option worked outside of Docker, but inside of Docker the restore would fail
# See https://docs.azul.com/core/crac/crac-debugging#restore-conflict-of-pids and https://docs.azul.com/core/crac/crac-debugging#using-cracminpid-option
# for info about -XX:CRaCMinPid
java -XX:CRaCMinPid=128 -XX:CRaCCheckpointTo=/opt/test-runner/crac-checkpoint -Xshare:off -jar /opt/test-runner/java-test-runner.jar $problem_slug . $output_folder
