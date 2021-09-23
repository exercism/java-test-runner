package com.exercism.xml;

import com.exercism.data.Report;
import com.exercism.data.TestDetails;
import com.exercism.xml.data.TestCase;
import com.exercism.xml.data.TestSuite;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class JUnitXmlParser {
    private final ImmutableMap<String, String> testCodeByTestName;
    private final Report.Builder report = Report.builder().setStatus("pass");

    public JUnitXmlParser(ImmutableMap<String, String> testCodeByTestName) {
        this.testCodeByTestName = testCodeByTestName;
    }

    public JUnitXmlParser parse(File file) {
        String xml;
        try {
            xml = Files.asCharSource(file, StandardCharsets.UTF_8).read();
        } catch (IOException e) {
            throw new IllegalStateException("Count not read file " + file.getAbsolutePath());
        }
        TestSuite testSuite;
        try {
            testSuite = XmlMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build()
                .readValue(xml, TestSuite.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                "Could not process XML for path " + file.getAbsolutePath(), e);
        }
        if (testSuite.failures > 0) {
            report.setStatus("fail");
        }
        for (TestCase tc : testSuite.testcase) {
            String fullMethodName = tc.classname + "." + tc.name;
            TestDetails.Builder testDetails = TestDetails.builder()
                .setName(fullMethodName)
                .setTestCode(
                    testCodeByTestName.getOrDefault(
                        fullMethodName, "Could not determine test code for " + fullMethodName))
                .setStatus("pass");
            if (tc.failure != null) {
                testDetails
                    .setStatus("fail")
                    .setMessage(
                        "Message: " + tc.failure.message + "\n"
                        + "Exception: " + tc.failure.value);
            }
            report.addTest(testDetails.build());
        }
        return this;
    }

    public Report buildReport() {
        return report.build();
    }
}
