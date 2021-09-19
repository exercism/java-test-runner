#!/bin/bash

if [ $# -lt 3 ]
then
    echo "Usage:"
    echo "./bin/run.sh two-fer ~/input/ ~/output/"
    exit 1
fi

problem_slug="$1"
input_folder="$2"
output_folder="$3"

cp -r $input_folder /opt/test-runner/lib/$problem_slug
cd /opt/test-runner/lib

find . -mindepth 1 -type f | grep 'Test.java' | xargs -I file sed -i "s/@Ignore(.*)//g;s/@Ignore//g;" file
cat <<EOF >settings.gradle
include '${problem_slug}'
EOF

echo "Running tests"
java -jar /opt/test-runner/autotest-runner.jar $problem_slug
mv results.json $output_folder
