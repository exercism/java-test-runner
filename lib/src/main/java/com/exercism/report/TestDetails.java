package com.exercism.report;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
@JsonDeserialize(builder = AutoValue_Report.Builder.class)
public abstract class TestDetails {
	@JsonProperty("name")
	public abstract String name();

	@JsonProperty("test_code")
	public abstract String testCode();

	@JsonProperty("status")
	public abstract String status();

	@JsonProperty("message")
	@Nullable
	public abstract String message();

	@JsonProperty("output")
	@Nullable
	public abstract String output();

	@JsonProperty("task_id")
	public abstract Optional<Integer> taskId();

	public static Builder builder() {
	  return new AutoValue_TestDetails.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		@JsonProperty("name")
		public abstract Builder setName(String name);

		@JsonProperty("test_code")
		public abstract Builder setTestCode(String testCode);

		@JsonProperty("status")
		public abstract Builder setStatus(String status);

		@JsonProperty("message")
		public abstract Builder setMessage(String message);

		@JsonProperty("output")
		public abstract Builder setOutput(String output);

		@JsonProperty("task_id")
		public abstract Builder setTaskId(int taskId);

		public abstract TestDetails build();
	}
}
