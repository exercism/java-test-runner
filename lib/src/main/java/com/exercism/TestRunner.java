package com.exercism;

import com.exercism.compiler.ExerciseCompilationException;
import com.exercism.compiler.ExerciseCompiler;
import com.exercism.report.Report;
import com.exercism.junit.JUnitTestParser;
import com.exercism.junit.JUnitTestRunner;
import com.exercism.report.ReportGenerator;
import com.exercism.report.ReportWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public final class TestRunner {

    private final ExerciseCompiler compiler;
    private final JUnitTestRunner testRunner;
    private final JUnitTestParser testParser;
    private final ReportWriter reportWriter;
    private final String inputDirectory;

    public TestRunner(String slug, String inputDirectory, String outputDirectory) {
        this.compiler = new ExerciseCompiler(slug);
        this.reportWriter = new ReportWriter(Paths.get(outputDirectory));
        this.testParser = new JUnitTestParser();
        this.testRunner = new JUnitTestRunner();
        this.inputDirectory = inputDirectory;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            throw new IllegalArgumentException("Not enough arguments, need <slug> <inputDirectory> <outputDirectory>");
        }
        new TestRunner(args[0], args[1], args[2]).run();
    }

    private void run() throws IOException {
        var sourceFiles = resolveSourceFiles(inputDirectory);
        var testFiles = resolveTestFiles(inputDirectory);

        for (File testFile : testFiles) {
            testParser.parse(testFile);
        }

        Report report;
        try {
            var compiledClasses = compiler.compile(Stream.concat(sourceFiles.stream(), testFiles.stream()).toList());
            testRunner.test(compiledClasses);
            report = ReportGenerator.generate(testRunner.getTestDetails(), testParser.buildTestCodeMap());
        } catch (ExerciseCompilationException e) {
            report = Report.builder()
                    .setStatus("error")
                    .setMessage(e.getMessage())
                    .build();
        }

        reportWriter.report(report);
    }

    private static Collection<File> resolveSourceFiles(String inputDirectory) throws IOException {
        var sourcePath = Paths.get(inputDirectory, "src", "main", "java");
        return resolveJavaFiles(sourcePath);
    }

    private static Collection<File> resolveTestFiles(String inputDirectory) throws IOException {
        var testPath = Paths.get(inputDirectory, "src", "test", "java");
        return resolveJavaFiles(testPath);
    }

    private static List<File> resolveJavaFiles(Path path) throws IOException {
        try (var files = Files.find(path, 10, (file, attrs) -> attrs.isRegularFile() && file.toString().endsWith(".java"))) {
            return files.map(Path::toFile).toList();
        }
    }
}
