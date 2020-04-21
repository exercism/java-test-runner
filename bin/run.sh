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

cd /opt/test-runner/lib

gradle --offline addListeners compileJava compileTestJava 2> /tmp/$compiler_error_log

cd /opt/test-runner/bin

if [ -s "/tmp/$compiler_error_log" ]
then 
    echo "Compiler error, processing"
    ./build_compilation_error_results.sh /tmp/$compiler_error_log > $output_folder/results.json
else
    echo "Running tests"
    cd /opt/test-runner/lib
    gradle --offline addListeners test
    mv results.json $output_folder
fi
