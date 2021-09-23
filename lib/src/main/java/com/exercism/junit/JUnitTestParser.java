package com.exercism.junit;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public final class JUnitTestParser {
    private final ImmutableMap.Builder<String, String> testCodeByTestName =
        ImmutableMap.builder();
    
    public JUnitTestParser parse(File file) {
        try {
            new JavaParser().parse(file)
                .ifSuccessful(compilationUnit -> parse(file, compilationUnit));
        } catch (IOException e) {
            throw new IllegalStateException("Could not read test file: " + file.getAbsolutePath(), e);
        }
        return this;
    }

    private void parse(File file, CompilationUnit compilationUnit) {
        String methodPrefix =
            compilationUnit.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElse("");

        String className = "";
        for (ClassOrInterfaceDeclaration classDeclaration
            : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
            className = classDeclaration.getNameAsString();
            break;
        }
        if (methodPrefix.isEmpty()) {
            methodPrefix = className;
        } else {
            methodPrefix = methodPrefix + "." + className;
        }

        for (MethodDeclaration methodDeclaration : compilationUnit.findAll(MethodDeclaration.class)) {
            if (!methodDeclaration.isAnnotationPresent​(Test.class)) {
                continue;
            }
            String fullMethodName = methodPrefix + "." + methodDeclaration.getNameAsString();
            testCodeByTestName.put(fullMethodName, methodDeclaration.toString());
        }
    }

    public ImmutableMap<String, String> buildTestCodeMap() {
        return testCodeByTestName.build();
    }
}
