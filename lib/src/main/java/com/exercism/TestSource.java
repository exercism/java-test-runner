package com.exercism;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record TestSource(String packageName, String className, String methodName) {
    public String identifier() {
        return Stream.of(packageName, className, methodName)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("."));
    }
}
