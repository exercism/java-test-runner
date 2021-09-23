package com.exercism.xml.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;

import java.util.List;

public class TestSuite {
    @JacksonXmlProperty(isAttribute = true)
    public String name;
    @JacksonXmlProperty(isAttribute = true)
    public int tests;
    @JacksonXmlProperty(isAttribute = true)
    public int skipped;
    @JacksonXmlProperty(isAttribute = true)
    public int failures;
    @JacksonXmlProperty(isAttribute = true)
    public int errors;
    @JacksonXmlProperty(isAttribute = true)
    public String timestamp;
    @JacksonXmlProperty(isAttribute = true)
    public String hostname;
    @JacksonXmlProperty(isAttribute = true)
    public double time;
    @JacksonXmlElementWrapper
    public List<Property> properties;
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<TestCase> testcase;
    @JacksonXmlProperty(localName = "system-out")
    public SystemOut systemOut;
    @JacksonXmlProperty(localName = "system-err")
    public SystemErr systemErr;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("tests", tests)
            .add("skipped", skipped)
            .add("failures", failures)
            .add("errors", errors)
            .add("hostname", hostname)
            .add("time", time)
            .add("testcase", testcase)
            .add("system-out", systemOut)
            .add("system-err", systemErr)
            .toString(); 
    }
}