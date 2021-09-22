#!/usr/bin/env bash

# Print out the commands to make this easier to debug
set -x

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

java -jar /opt/test-runner/autotest-runner.jar $problem_slug
cat gradle-test.err
cat results.json
mv results.json $output_folder
