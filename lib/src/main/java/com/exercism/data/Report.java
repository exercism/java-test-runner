package com.exercism.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;

@AutoValue
@JsonDeserialize(builder = AutoValue_Report.Builder.class)
public abstract class Report {
	@JsonProperty("status")
	public abstract String status();

	@JsonProperty("message")
	public abstract String message();

	@JsonProperty("tests")
	public abstract ImmutableList<TestDetails> tests();

	@JsonProperty("version")
	public abstract int version();

	public static Builder builder() {
	  return new AutoValue_Report.Builder();
	}

	@AutoValue.Builder
	public abstract class Builder {
		@JsonProperty("status")
		public abstract Builder setStatus(String status);

		@JsonProperty("message")
		public abstract Builder setMessage(String message);

		@JsonProperty("tests")
		public abstract Builder setTests(List<TestDetails> tests);

		public abstract ImmutableList.Builder<TestDetails> testsBuilder();

		public final Builder addTest(TestDetails test) {
			testsBuilder().add(test);
			return this;
		}

		@JsonProperty("version")
		public abstract Builder setVersion(int version);

		public abstract Report build();
	}
}
