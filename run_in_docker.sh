#!/bin/bash

if [ $# -lt 3 ]
then
    echo "Usage:"
    echo "./run_in_docker.sh two-fer ~/input/ ~/output/"
    exit 1
fi

Exercise="$1"
InputDirectory="$2"
OutputDirectory="$3"

docker build -t exercism/java-test-runner .

time docker run \
    --network none \
    --mount type=bind,src="${InputDirectory}",dst=/solution \
    --mount type=bind,src="${OutputDirectory}",dst=/results \
    --mount type=tmpfs,dst=/tmp \
    exercism/java-test-runner "${Exercise}" /solution /results
