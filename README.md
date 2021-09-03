# Exercism Java test runner

A test runner automatically verifies if a submission passes all the tests.

This repository contains the Java test runner, which implements the [test runner interface][test-runner-interface].

## Running the tests

To run a solution's tests, follow these steps:
 
1. Open a command prompt in the root directory.
1. Run `./run_in_docker.sh <exercise> <input-directory> <output-directory>`. This script will:
   1. Make sure all tests run (no skipped tests) for the solution found in `<input-directory>`.
   1. Run all the tests.
   1. Once the script has completed, the test results will be written to `<output-directory>/results.json`.

## Running the tests using Docker

To run a solution's tests using a Docker container, follow these steps:

1. Open a command prompt in the root directory.
1. Run `./run_in_docker.sh <exercise> <input-directory> <output-directory>`. This script will:
   1. Make sure all tests run (no skipped tests) for the solution found in `<input-directory>`.
   1. Run all the tests.
   1. Once the script has completed, the test results will be written to `<output-directory>/results.json`.


[test-runner-interface]: https://github.com/exercism/docs/blob/main/building/tooling/test-runners/interface.md
