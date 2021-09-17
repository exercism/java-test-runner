package com.exercism.runner;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Arguments {
    public abstract String slug();

    public abstract String inputDirectory();

    public abstract String outputDirectory();

    public static Builder builder() {
        return new AutoValue_Arguments.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setSlug(String slug);

        public abstract Builder setInputDirectory(String inputDirectory);

        public abstract Builder setOutputDirectory(String outputDirectory);

        public abstract Arguments build();
    }
}
