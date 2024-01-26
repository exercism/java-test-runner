package com.exercism;

import java.util.Optional;

public record TestMetadata(String name, Optional<Integer> taskId) {
}
