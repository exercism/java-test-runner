package com.exercism.junit;

import com.exercism.TestDetails;
import com.exercism.TestResult;
import com.exercism.TestSource;
import com.exercism.TestStatus;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.util.*;

public class JUnitTestRunner implements TestExecutionListener {

    private final Map<String, String> outputPerTest;
    private final List<TestDetails> testDetails;

    public JUnitTestRunner() {
        this.outputPerTest = new HashMap<>();
        this.testDetails = new ArrayList<>();
    }

    public void test(Collection<Class<?>> classes) {
        var selectors = classes.stream().map(DiscoverySelectors::selectClass).toArray(DiscoverySelector[]::new);
        var request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectors)
                .configurationParameter("junit.platform.output.capture.stdout", "true")
                .build();
        var launcher = LauncherFactory.create();
        var testPlan = launcher.discover(request);
        launcher.execute(testPlan, this);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (!testIdentifier.isTest()) {
            return;
        }

        var id = testIdentifier.getUniqueId();
        String output = null;
        if (outputPerTest.containsKey(id)) {
            output = outputPerTest.get(id);
        }

        var result = new TestResult(mapStatus(testExecutionResult.getStatus()), testExecutionResult.getThrowable());
        var details = new TestDetails(getTestSource(testIdentifier), result, testIdentifier.getDisplayName(), output);
        testDetails.add(details);
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        for (Map.Entry<String, String> pair : entry.getKeyValuePairs().entrySet()) {
            if (pair.getKey().equals("stdout")) {
                var output = pair.getValue();
                outputPerTest.merge(testIdentifier.getUniqueId(), output, (s1, s2) -> s1 + s2);
            }
        }
    }

    public List<TestDetails> getTestDetails() {
        return testDetails;
    }

    private static TestStatus mapStatus(TestExecutionResult.Status status) {
        return switch (status) {
            case SUCCESSFUL -> TestStatus.PASS;
            case FAILED -> TestStatus.FAIL;
            case ABORTED -> TestStatus.ERROR;
        };
    }

    private static TestSource getTestSource(TestIdentifier identifier) {
        return identifier.getSource()
                .flatMap(ts -> ts instanceof MethodSource ms ? Optional.of(ms) : Optional.empty())
                .map(JUnitTestRunner::mapTestSource)
                .orElseGet(() ->
                        new TestSource(
                                null,
                                identifier.getUniqueIdObject().removeLastSegment().getLastSegment().getValue(),
                                identifier.getUniqueIdObject().getLastSegment().getValue()
                        )
                );
    }

    private static TestSource mapTestSource(MethodSource methodSource) {
        return new TestSource(
                methodSource.getJavaClass().getPackageName(),
                methodSource.getClassName(),
                methodSource.getMethodName()
        );
    }
}
