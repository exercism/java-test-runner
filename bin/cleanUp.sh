#!/bin/bash

if [ $# -lt 2 ]
then
    echo "Usage:"
    echo "./bin/setUpSolution.sh problem_slug input_folder"
    exit 1
fi

problem_slug="$1"
input_folder="$2"

## CleanUp Input Volume
mv $input_folder/$problem_slug/build.gradle.back $input_folder/$problem_slug/build.gradle
rm -rf $input_folder/$problem_slug/buildSrc