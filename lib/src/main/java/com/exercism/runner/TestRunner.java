package com.exercism.runner;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.exercism.data.Report;
import com.exercism.junit.JUnitTestParser;
import com.exercism.report.ReportGenerator;
import com.exercism.xml.JUnitXmlParser;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.common.io.MoreFiles;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TestRunner {
    private static final String GRADLE_TEST_ERR = "gradle-test.err";

    public static void main(String[] args) throws InterruptedException, IOException {
        run(args[0]);
    }

    private static void run(String returnCode) throws InterruptedException, IOException {
        if (returnCode != "0") {
            String gradleErrorOutput = Files.asCharSource(
                Paths.get(GRADLE_TEST_ERR).toFile(), StandardCharsets.UTF_8)
                .read();
            if (gradleErrorOutput.contains("compileJava")) {
                ReportGenerator.report(
                    Report.builder()
                        .setStatus("error")
                        .setMessage(gradleErrorOutput)
                        .build());
                return;
            }
        }

        JUnitTestParser testParser = new JUnitTestParser();
        for (Path filePath : MoreFiles.listFiles(Paths.get("src", "test", "java"))) {
            if (MoreFiles.getFileExtension(filePath).equals("java")) {
                testParser.parse(filePath.toFile());
            }
        }

        ImmutableMap<String, String> testCodeByTestName = testParser.buildTestCodeMap();
        JUnitXmlParser xmlParser = new JUnitXmlParser(testCodeByTestName);
        for (Path filePath : MoreFiles.listFiles(Paths.get("build", "test-results", "test"))) {
            if (MoreFiles.getFileExtension(filePath).equals("xml")) {
                xmlParser.parse(filePath.toFile());
            }
        }

        Report report = xmlParser.buildReport();
        ReportGenerator.report(report);
    }
}
