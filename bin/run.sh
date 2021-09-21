#!/bin/bash

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

rm -rf $output_folder/*
mkdir -p $output_folder/$problem_slug
cp -r $input_folder/* $output_folder/$problem_slug
cd $output_folder/$problem_slug

find . -mindepth 1 -type f | grep 'Test.java' | xargs -I file sed -i "s/@Ignore(.*)//g;s/@Ignore//g;" file

export GRADLE_USER_HOME=/opt/test-runner/gradle/.gradle
java -jar /opt/test-runner/autotest-runner.jar $problem_slug
cat gradle-test.err
cat results.json
mv results.json $output_folder
