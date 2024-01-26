package com.exercism.junit;

import com.exercism.TestSource;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.ImmutableMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class JUnitTestParser {
    private final ImmutableMap.Builder<TestSource, String> testCodeByTestName = ImmutableMap.builder();

    public void parse(File file) {
        try {
            new JavaParser().parse(file).ifSuccessful(this::parse);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read test file: " + file.getAbsolutePath(), e);
        }
    }

    private void parse(CompilationUnit compilationUnit) {
        var packageName =
                compilationUnit.getPackageDeclaration()
                        .map(PackageDeclaration::getNameAsString)
                        .orElse("");

        for (MethodDeclaration methodDeclaration : compilationUnit.findAll(MethodDeclaration.class)) {
            if (!methodDeclaration.isAnnotationPresent(org.junit.jupiter.api.Test.class)) {
                continue;
            }
            var methodName = methodDeclaration.getNameAsString();
            var className = getClassName(methodDeclaration);
            var testSource = new TestSource(packageName, className, methodName);
            testCodeByTestName.put(testSource, methodDeclaration.toString());
        }
    }

    private static String getClassName(MethodDeclaration testMethod) {
        var classNames = new ArrayList<String>();
        var parentNode = testMethod.getParentNode();

        while (parentNode.isPresent() && parentNode.get() instanceof ClassOrInterfaceDeclaration parentClass) {
            classNames.add(parentClass.getNameAsString());
            parentNode = parentClass.getParentNode();
        }

        return String.join("$", classNames.reversed());
    }

    public ImmutableMap<TestSource, String> buildTestCodeMap() {
        return testCodeByTestName.build();
    }
}
