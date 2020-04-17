#!/bin/bash

if [ $# -lt 3 ]
then
    echo "Usage:"
    echo "./bin/run.sh two_fer ~/input/ ~/output/"
    exit 1
fi

problem_slug="$1"
input_folder="$2"
output_folder="$3"
compiler_error_log="compiler.log"

## Setup Input Volume
./bin/setUpSolution.sh $problem_slug $input_folder 

cd /opt/test-runner/lib
gradle --offline compileJava compileTestJava 2> /opt/test-runner/bin/$compiler_error_log
cd /opt/test-runner/bin


if [ -s "$compiler_error_log" ]
then 
    echo "Compiler error, processing"
    ./build_compilation_error_results.sh $compiler_error_log > $output_folder/results.json
else
    echo "Running tests"
    cd /opt/test-runner/lib
    gradle --offline test
    mv results.json $output_folder
fi
