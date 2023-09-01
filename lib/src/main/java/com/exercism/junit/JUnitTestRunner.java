package com.exercism.junit;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.util.*;

public class JUnitTestRunner implements TestExecutionListener {

    private final Map<String, String> outputPerTest;
    private final List<JUnitTestDetails> testDetails;

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

        testDetails.add(new JUnitTestDetails(testIdentifier, testExecutionResult, output));
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

    public List<JUnitTestDetails> getTestDetails() {
        return testDetails;
    }
}
