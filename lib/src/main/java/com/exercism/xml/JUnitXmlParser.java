package com.exercism.xml;

import com.exercism.data.Report;
import com.exercism.data.TestDetails;
import com.exercism.xml.data.TestCase;
import com.exercism.xml.data.TestSuite;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public final class JUnitXmlParser {
    private final Report.Builder report = Report.builder().setStatus("pass").setVersion(1);

    public JUnitXmlParser parse(String path) {
        String xml;
        try {
            xml = inputStreamToString(new FileInputStream(Paths.get(path).toFile()));
        } catch (IOException e) {
            throw new IllegalStateException("Count not read file " + path);
        }
        TestSuite testSuite;
        try {
            testSuite = XmlMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build()
                .readValue(xml, TestSuite.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not process XML for path " + path, e);
        }
        if (testSuite.failures > 0) {
            report.setStatus("fail");
        }
        for (TestCase tc : testSuite.testcase) {
            TestDetails.Builder testDetails = TestDetails.builder()
                .setName(tc.name)
                .setStatus("pass");
            if (tc.failure != null) {
                testDetails
                    .setStatus("fail")
                    .setMessage(
                        "Message: " + tc.failure.message + "\n"
                        + "Exception: " + tc.failure.value);
                report.setMessage(tc.failure.message);
            }
            report.addTest(testDetails.build());
        }
        return this;
    }
    
    private static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }

    public Report buildReport() {
        return report.build();
    }
}
