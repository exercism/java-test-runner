package com.exercism.junit;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestIdentifier;

public record JUnitTestDetails(TestIdentifier identifier, TestExecutionResult result, String output) {
}
