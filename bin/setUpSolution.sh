#!/bin/bash

if [ $# -lt 2 ]
then
    echo "Usage:"
    echo "./bin/setUpSolution.sh problem_slug input_folder"
    exit 1
fi

problem_slug="$1"
input_folder="$2"

## Setup Input Volume
cp -r lib/buildSrc $input_folder/$problem_slug/
cp $input_folder/$problem_slug/build.gradle $input_folder/$problem_slug/build.gradle.back

echo "" >> $input_folder/$problem_slug/build.gradle # make sure imports are added on a new line
echo "gradle.addListener(new com.exercism.gradle.ExerciseTestListener())" >> $input_folder/$problem_slug/build.gradle
echo "gradle.addListener(new com.exercism.gradle.ExercismTestOutputListener())" >> $input_folder/$problem_slug/build.gradle
echo "gradle.addListener(new com.exercism.gradle.ExerciseTaskExecutionListener())" >> $input_folder/$problem_slug/build.gradle
