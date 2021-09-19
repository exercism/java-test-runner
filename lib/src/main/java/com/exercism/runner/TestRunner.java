package com.exercism.runner;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.exercism.data.Report;
import com.exercism.report.ReportGenerator;
import com.exercism.xml.JUnitXmlParser;
import com.google.common.io.MoreFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TestRunner {
    public static void main(String[] args) throws InterruptedException, IOException {
        run(
            Arguments.builder()
                .setSlug("")//args[0])
                .setOutputDirectory("")//args[1])
                .build());
    }

    private static void run(Arguments args) throws InterruptedException, IOException {
        // TODO: run gradle test --offline
        Process gradleTest = new ProcessBuilder("gradle", "test", "--offline")
            .redirectOutput(new File("gradle-test.out"))
            .redirectError(new File("gradle-test.err"))
            .start();
        if (!gradleTest.waitFor(10, SECONDS)) {
            throw new IllegalStateException("gradle test did not complete within 10 seconds");
        }
        if (gradleTest.exitValue() != 0) {
            throw new IllegalStateException("gradle test completed with an error");
        }
        JUnitXmlParser parser = new JUnitXmlParser();
        for (Path filePath : MoreFiles.listFiles(Paths.get("/workspace/java-test-runner/temp/build/test-results/test/"))) {
            if (filePath.endsWith(".xml")) {
                System.err.println(filePath);
                parser.parse(filePath.toFile().getAbsolutePath());
            }
        }
        Report report = parser.buildReport();
        ReportGenerator.report(report);
    }
}
