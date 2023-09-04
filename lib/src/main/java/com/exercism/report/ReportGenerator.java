package com.exercism.report;

import com.exercism.TestDetails;
import com.exercism.TestStatus;
import com.google.common.base.Throwables;

import java.util.Collection;
import java.util.Map;

public class ReportGenerator {
    public static Report generate(Collection<TestDetails> testDetails, Map<String, String> testCodeMap) {
        var reportDetails = testDetails.stream().map(item -> buildTestDetails(item, testCodeMap)).toList();
        var reportStatus = collapseStatuses(testDetails.stream().map(details -> details.result().status()).toList());
        var reportVersion = testDetails.stream().anyMatch(details -> details.metadata().taskId().isPresent()) ? 3 : 2;

        return Report.builder()
                .setTests(reportDetails)
                .setStatus(mapStatus(reportStatus))
                .setVersion(reportVersion)
                .build();
    }

    private static com.exercism.report.TestDetails buildTestDetails(TestDetails testDetails, Map<String, String> testCodeMap) {
        var detailBuilder = com.exercism.report.TestDetails.builder()
                .setStatus(mapStatus(testDetails.result().status()))
                .setTestCode(testCodeMap.get(testDetails.source().identifier()))
                .setName(testDetails.metadata().name())
                .setOutput(testDetails.output());

        testDetails.result().failure().ifPresent(t -> {
            var message = String.format("Message: %s%nException: %s", t.getMessage(), Throwables.getStackTraceAsString(t));
            detailBuilder.setMessage(message);
        });

        testDetails.metadata().taskId().ifPresent(detailBuilder::setTaskId);

        return detailBuilder.build();
    }

    private static String mapStatus(TestStatus status) {
        return switch (status) {
            case PASS -> "pass";
            case FAIL -> "fail";
            case ERROR -> "error";
        };
    }

    private static TestStatus collapseStatuses(Collection<TestStatus> statuses) {
        for (TestStatus status : statuses) {
            if (status == TestStatus.ERROR) {
                return TestStatus.ERROR;
            }

            if (status == TestStatus.FAIL) {
                return TestStatus.FAIL;
            }
        }

        return TestStatus.PASS;
    }
}
