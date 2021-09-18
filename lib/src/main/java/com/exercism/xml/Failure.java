package com.exercism.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.auto.value.AutoValue;
import com.google.auto.value.AutoValue.Builder;

@AutoValue
@JsonDeserialize(builder = AutoValue_Failure.Builder.class)
public abstract class Failure {
    @JacksonXmlProperty(isAttribute = true, localName = "message")
    public abstract String message();

    @JacksonXmlProperty(isAttribute = true, localName = "type")
    public abstract String type();

    @JacksonXmlProperty
    public abstract String value();

    public static Builder builder() {
        return new AutoValue_Failure.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JacksonXmlProperty(localName = "message")
        public abstract Builder setMessage(String message);
    
        @JacksonXmlProperty(localName = "type")
        public abstract Builder setType(String type);
    
        @JacksonXmlProperty
        public abstract Builder setValue(String value);

        public abstract Failure build();
    }
}
