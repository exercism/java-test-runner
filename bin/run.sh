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
# ./bin/run-in-docker.sh two-fer path/to/solution/folder/ path/to/output/directory/

if [ $# -lt 3 ]
then
    echo "Usage:"
    echo "./bin/run.sh two-fer ~/input/ ~/output/"
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

find . -mindepth 1 -type f | grep 'Test.java' | xargs -I file sed -i "s/@Ignore(.*)//g;s/@Ignore//g;" file

cp /root/pom.xml .

java -jar /opt/test-runner/autotest-runner.jar $problem_slug
mv results.json $output_folder
