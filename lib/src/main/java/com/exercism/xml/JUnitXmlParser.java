package com.exercism.xml;

import com.exercism.data.Report;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import org.openmbee.junit.JUnitMarshalling;

public final class JUnitXmlReportParser {
    private final Report.Builder report = Report.builder().setStatus("pass");

    public JUnitXmlReportParser parse(String path) {
        TestSuite testSuite;
        try {
            FileInputStream fileInputStream = new FileInputStream(Paths.get(path).toFile());
            testSuite = JUnitMarshalling.unmarshalTestSuite(fileInputStream);
        } catch (IOException | JAXBException | XMLStreamException e) {
            throw new IllegalStateException("Count not read file " + path);
        }
        if (testSuite.failures() > 0) {
            report.setStatus("fail");
        }
        return this;
    }

    public Report buildReport() {
        return report.build();
    }
}
