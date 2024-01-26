package com.exercism;

import java.util.Optional;

public record TestResult(TestStatus status, Optional<Throwable> failure) {
}
