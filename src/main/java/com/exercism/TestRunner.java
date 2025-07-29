package com.exercism;

import com.exercism.compiler.ExerciseCompilationException;
import com.exercism.compiler.ExerciseCompiler;
import com.exercism.junit.JUnitTestParser;
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

import org.crac.CheckpointException;
import org.crac.Core;
import org.crac.RestoreException;

public final class TestRunner {

    private final JUnitTestParser testParser;
    private final JUnitTestRunner testRunner;
    private final ReportWriter reportWriter;
    private final String slug;
    private final String inputDirectory;

    public TestRunner(String slug, String inputDirectory, String outputDirectory) {
        this.testParser = new JUnitTestParser();
        this.reportWriter = new ReportWriter(Paths.get(outputDirectory));
        this.testRunner = new JUnitTestRunner();
        this.slug = slug;
        this.inputDirectory = inputDirectory;
    }

    public static void main(String[] args) throws IOException, CheckpointException, RestoreException {
        if (args.length < 3) {
            throw new IllegalArgumentException("Not enough arguments, need <slug> <inputDirectory> <outputDirectory>");
        }
        new TestRunner(args[0], args[1], args[2]).run();

        Core.checkpointRestore();
    }

    void run() throws IOException {
        var sourceFiles = resolveSourceFiles();
        var testFiles = resolveTestFiles();

        for (File testFile : testFiles) {
            testParser.parse(testFile);
        }

        var filesToCompile = Stream.concat(sourceFiles.stream(), testFiles.stream()).toList();

        Report report;
        try (var compiler = new ExerciseCompiler(slug)) {
            compiler.compile(filesToCompile);
            testRunner.test(compiler.getClassLoader(), compiler.getClasspathRoots());
            report = ReportGenerator.generate(testRunner.getTestDetails(), testParser.buildTestCodeMap());
        } catch (ExerciseCompilationException e) {
            report = Report.builder()
                    .setStatus("error")
                    .setMessage(e.getMessage())
                    .build();
        }

        reportWriter.report(report);
    }

    private Collection<File> resolveSourceFiles() throws IOException {
        var sourcePath = Paths.get(inputDirectory, "src", "main", "java");
        return resolveJavaFiles(sourcePath);
    }

    private Collection<File> resolveTestFiles() throws IOException {
        var testPath = Paths.get(inputDirectory, "src", "test", "java");
        return resolveJavaFiles(testPath);
    }

    private static List<File> resolveJavaFiles(Path path) throws IOException {
        try (var files = Files.find(path, 10, (file, attrs) -> attrs.isRegularFile() && file.toString().endsWith(".java"))) {
            return files.map(Path::toFile).toList();
        }
    }
}
