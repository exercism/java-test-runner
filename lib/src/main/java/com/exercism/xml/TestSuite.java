package com.exercism.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_TestSuite.Builder.class)
@JacksonXmlRootElement(localName = "testsuite")
public abstract class TestSuite {
    @JacksonXmlProperty(isAttribute = true, localName = "errors")
    public abstract Integer errors();

    @JacksonXmlProperty(isAttribute = true, localName = "failures")
    public abstract int failures();

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    public abstract String name();

    @JacksonXmlProperty(localName = "package")
    public abstract String pakkage();

    @JacksonXmlProperty(isAttribute = true, localName = "skipped")
    public abstract int skipped();

    @JacksonXmlProperty(isAttribute = true, localName = "tests")
    public abstract int tests();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "testcase")
    public abstract ImmutableList<TestCase> testCases();

    @JacksonXmlProperty(localName = "system-out")
    public abstract String systemOut();

    @JacksonXmlProperty(localName = "system-err")
    public abstract String systemErr();

    public static Builder builder() {
        return new AutoValue_TestSuite.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JacksonXmlProperty(isAttribute = true, localName = "errors")
        public abstract Builder setErrors(int errors);

        @JacksonXmlProperty(isAttribute = true, localName = "failures")
        public abstract Builder setFailures(int failures);

        @JacksonXmlProperty(isAttribute = true, localName = "name")
        public abstract Builder setName(String name);

        @JacksonXmlProperty(localName = "package")
        public abstract Builder setPakkage(String pakkage);

        @JacksonXmlProperty(isAttribute = true, localName = "skipped")
        public abstract Builder setSkipped(int skipped);

        @JacksonXmlProperty(isAttribute = true, localName = "tests")
        public abstract Builder setTests(int tests);

        @JacksonXmlProperty(localName = "testcase")
        public abstract Builder setTestCases(List<TestCase> testCases);

        @JacksonXmlProperty(localName = "system-out")
        public abstract Builder setSystemOut(String systemOut);

        @JacksonXmlProperty(localName = "system-err")
        public abstract Builder setSystemErr(String systemErr);

        public abstract TestSuite build();
    }
}