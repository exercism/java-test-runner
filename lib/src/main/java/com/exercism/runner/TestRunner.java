package com.exercism.runner;

import com.exercism.compiler.ExerciseCompilationException;
import com.exercism.compiler.ExerciseCompiler;
import com.exercism.data.Report;
import com.exercism.data.TestDetails;
import com.exercism.junit.JUnitTestDetails;
import com.exercism.junit.JUnitTestParser;
import com.exercism.junit.JUnitTestRunner;
import com.exercism.report.ReportGenerator;
import org.junit.platform.engine.TestExecutionResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class TestRunner {

    private final ExerciseCompiler compiler;
    private final JUnitTestRunner testRunner;
    private final JUnitTestParser testParser;
    private final ReportGenerator reportGenerator;
    private final String inputDirectory;

    public TestRunner(String slug, String inputDirectory, String outputDirectory) {
        this.compiler = new ExerciseCompiler(slug);
        this.reportGenerator = new ReportGenerator(Paths.get(outputDirectory));
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
            report = buildReport(testRunner.getTestDetails(), testParser.buildTestCodeMap());
        } catch (ExerciseCompilationException e) {
            report = Report.builder()
                    .setStatus("error")
                    .setMessage(e.getMessage())
                    .build();
        }

        reportGenerator.report(report);
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
        try (var files = Files.find(path, 2, (file, attrs) -> attrs.isRegularFile() && file.toString().endsWith(".java"))) {
            return files.map(Path::toFile).toList();
        }
    }

    private static Report buildReport(List<JUnitTestDetails> jUnitTestDetails, Map<String, String> testCodeMap) {
        var builder = Report.builder();

        for (JUnitTestDetails testDetails : jUnitTestDetails) {
            var testClassName = testDetails.identifier().getUniqueIdObject().removeLastSegment().getLastSegment().getValue();
            var testName = String.format("%s.%s", testClassName, testDetails.identifier().getDisplayName());

            var detailBuilder = TestDetails.builder()
                    .setStatus(testDetails.result().getStatus() == TestExecutionResult.Status.SUCCESSFUL ? "pass" : "fail")
                    .setTestCode(testCodeMap.get(testName))
                    .setName(testName)
                    .setOutput(testDetails.output());

            testDetails.result().getThrowable().ifPresent(t -> detailBuilder.setMessage(t.getMessage()));

            var detail = detailBuilder.build();

            builder.addTest(detail);
            builder.setStatus(detail.status());
        }

        return builder.build();
    }
}
