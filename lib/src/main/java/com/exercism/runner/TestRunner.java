package com.exercism.runner;

import com.exercism.data.Report;
import com.exercism.report.ReportGenerator;

public final class TestRunner {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("not enough args");
        }
        run(
            Arguments.builder()
                .setSlug(args[0])
                .setOutputDirectory(args[1])
                .build());
    }

    private static void run(Arguments args) {
        // TODO: run gradle test
        // TODO: parse the test results into a Report object
        Report report = Report.builder().build();
        ReportGenerator.report(report);
    }
}
