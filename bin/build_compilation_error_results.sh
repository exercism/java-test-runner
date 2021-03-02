#!/bin/bash

function Get_Failure_Message {
   echo $(grep -h "Execution failed for task" "$1")
}
function Get_Errors {
    echo "$(sed -e '/[0-9]\{1,\} error/,$d' "$1")"
}

FAILURE_MESSAGE=$(Get_Failure_Message $1)
ERRORS=$(Get_Errors $1)

JSON_STRING=$( jq -n \
                  --arg status "error" \
                  --arg message "$FAILURE_MESSAGE $ERRORS" \
                  '{status: $status, message: $message}' )

echo "$JSON_STRING"
