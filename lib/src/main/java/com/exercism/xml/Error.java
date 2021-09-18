package com.exercism.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_Error.Builder.class)
public abstract class Error {
    @JsonProperty("message")
    public abstract String message();

    @JsonProperty("type")
    public abstract String type();

    @JsonProperty("value")
    public abstract String value();

    public static Builder builder() {
        return new AutoValue_Error.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty("message")
        public abstract Builder setMessage(String message);
    
        @JsonProperty("type")
        public abstract Builder setType(String type);
    
        @JsonProperty("value")
        public abstract Builder setValue(String value);

        public abstract Error build();
    }
}
