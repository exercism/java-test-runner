package com.exercism.xml;

import com.exercism.data.Report;
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
    private final Report.Builder report = Report.builder().setStatus("pass");

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
                .defaultUseWrapper(true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build()
                .readValue(xml, TestSuite.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not process XML path " + path, e);
        }
        System.out.println(testSuite);
        if (testSuite.failures() > 0) {
            report.setStatus("fail");
        }
        return this;
    }
    
    private static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public Report buildReport() {
        return report.build();
    }
}
