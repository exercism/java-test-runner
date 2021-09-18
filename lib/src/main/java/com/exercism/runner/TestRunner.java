package com.exercism.runner;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.exercism.data.Report;
import com.exercism.report.ReportGenerator;
import com.exercism.xml.JUnitXmlParser;
import java.io.IOException;

public final class TestRunner {
    public static void main(String[] args) throws InterruptedException, IOException {
        Report report = new JUnitXmlParser()
            .parse("/workspace/java-test-runner/temp/build/test-results/test/TEST-jason.JasonTest.xml")
            .buildReport();
        ReportGenerator.report(report);
        if (args.length < 2) {
            throw new IllegalArgumentException("not enough args");
        }
        run(
            Arguments.builder()
                .setSlug(args[0])
                .setOutputDirectory(args[1])
                .build());
    }

    private static void run(Arguments args) throws InterruptedException, IOException {
        // TODO: run gradle test --offline
        Process gradleTest = new ProcessBuilder("gradle", "test", "--offline").start();
        if (!gradleTest.waitFor(10, SECONDS)) {
            throw new IllegalStateException("gradle test did not complete within 10 seconds");
        }
        if (gradleTest.exitValue() != 0) {
            throw new IllegalStateException("gradle test completed with an error");
        }
        Report report = new JUnitXmlParser()
            .parse("/workspace/java-test-runner/temp/build/test-results/test/TEST-jason.JasonTest.xml")
            .buildReport();
        ReportGenerator.report(report);
    }
}
