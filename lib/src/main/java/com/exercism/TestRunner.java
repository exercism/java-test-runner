package com.exercism;

import com.exercism.compiler.ExerciseCompilationException;
import com.exercism.compiler.ExerciseCompiler;
import com.exercism.junit.JUnitTestRunner;
import com.exercism.report.Report;
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

    private final JavaSourceFileAnalyzer analyzer;
    private final ExerciseCompiler compiler;
    private final JUnitTestRunner testRunner;
    private final ReportWriter reportWriter;
    private final String inputDirectory;

    public TestRunner(String slug, String inputDirectory, String outputDirectory) {
        this.analyzer = new JavaSourceFileAnalyzer();
        this.compiler = new ExerciseCompiler(slug);
        this.reportWriter = new ReportWriter(Paths.get(outputDirectory));
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

        for (File sourceFile : sourceFiles) {
            analyzer.analyze(sourceFile);
        }

        Report report;
        try {
            var compiledClasses = compiler.compile(sourceFiles, analyzer.getClasses());
            testRunner.test(compiledClasses);
            report = ReportGenerator.generate(testRunner.getTestDetails(), analyzer.getMethodDeclarations());
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
        var testPath = Paths.get(inputDirectory, "src", "test", "java");
        return Stream.concat(resolveJavaFiles(sourcePath).stream(), resolveJavaFiles(testPath).stream()).toList();
    }

    private static List<File> resolveJavaFiles(Path path) throws IOException {
        try (var files = Files.find(path, 10, (file, attrs) -> attrs.isRegularFile() && file.toString().endsWith(".java"))) {
            return files.map(Path::toFile).toList();
        }
    }
}
