#!/bin/bash

if [ $# -lt 3 ]
then
    echo "Usage:"
    echo "./bin/run.sh two_fer ~/input/ ~/output/"
    exit 1
fi

Exercise="$1"
InputDirectory="$2"
OutputDirectory="$3"

docker build -t exercism/java-test-runner .
docker run -v ${InputDirectory}:/solution/ -v ${OutputDirectory}:/results/ exercism/java-test-runner $Exercise /solution /results
