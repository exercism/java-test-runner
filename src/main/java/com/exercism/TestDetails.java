package com.exercism;

public record TestDetails(TestSource source, TestMetadata metadata, TestResult result, String output) {
}
