#!/bin/bash

if [ $# -lt 2 ]
then
    echo "Usage:"
    echo "./bin/setUpSolution.sh problem_slug input_folder"
    exit 1
fi

problem_slug="$1"
input_folder="$2"

mv  /opt/test-runner/.gradle/* /home/gradle/.gradle/
cp -r $input_folder/$problem_slug/* /opt/test-runner/lib/

cp /opt/test-runner/lib/build.gradle /opt/test-runner/lib/build.gradle.back

echo "" >> /opt/test-runner/lib/build.gradle # make sure imports are added on a new line
echo "gradle.addListener(new com.exercism.gradle.ExerciseTestListener())" >> /opt/test-runner/lib/build.gradle
echo "gradle.addListener(new com.exercism.gradle.ExercismTestOutputListener())" >> /opt/test-runner/lib/build.gradle
echo "gradle.addListener(new com.exercism.gradle.ExerciseTaskExecutionListener())" >> /opt/test-runner/lib/build.gradle
