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
home_dir=$(pwd)
compiler_error_log="compiler.log"

## Setup Input Volume
./bin/setUpSolution.sh $problem_slug $input_folder 

cd $input_folder/$problem_slug
gradle compileJava compileTestJava 2> $home_dir/$compiler_error_log
cd $home_dir


if [ -s "$compiler_error_log" ]
then 
    echo "Compiler error, processing"
    ./bin/build_compilation_error_results.sh $compiler_error_log > $output_folder/results.json
else
    echo "Running tests"
    cd $input_folder/$problem_slug
    gradle clean test
    mv results.json $output_folder
fi

## CleanUp Input Volume
cd $home_dir
./bin/cleanUp.sh $problem_slug $input_folder
