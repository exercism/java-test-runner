package com.exercism.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.Builder;
import com.google.common.collect.ImmutableList;
import java.util.List;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_TestCase.Builder.class)
public abstract class TestCase {
    @JacksonXmlProperty(isAttribute = true, localName = "classname")
    public abstract String className();

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    public abstract String name();

    @JacksonXmlProperty(localName = "status")
    public abstract String status();

    @JacksonXmlProperty(localName = "error")
    public abstract ImmutableList<Error> errors();

    @JacksonXmlElementWrapper
    @JacksonXmlProperty(localName = "failure")
    public abstract ImmutableList<Failure> failures();

    @JacksonXmlProperty(localName = "system-out")
    public abstract String systemOut();

    @JacksonXmlProperty(localName = "system-err")
    public abstract String systemErr();

    public static Builder builder() {
        return new AutoValue_TestCase.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "set")
    public abstract static class Builder {
        @JacksonXmlProperty(isAttribute = true, localName = "classname")
        public abstract Builder setClassName(String className);

        @JacksonXmlProperty(isAttribute = true, localName = "name")
        public abstract Builder setName(String name);

        @JacksonXmlProperty(localName = "status")
        public abstract Builder setStatus(String status);

        @JacksonXmlProperty(localName = "error")
        public abstract Builder setErrors(List<Error> errors);

        @JacksonXmlElementWrapper
        @JacksonXmlProperty(localName = "failure")
        public abstract Builder setFailures(List<Failure> failures);

        @JacksonXmlProperty(localName = "system-out")
        public abstract Builder setSystemOut(String systemOut);

        @JacksonXmlProperty(localName = "system-err")
        public abstract Builder setSystemErr(String systemErr);

        public abstract TestCase build();
    }
}
