package com.exercism.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestDetails {
	public abstract String name();

	public abstract String status();

	public abstract String message();

	public abstract String output();

	public static Builder builder() {
	  return new AutoValue_TestDetails.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder setName(String name);

		public abstract Builder setStatus(String status);

		public abstract Builder setMessage(String message);

		public abstract Builder setOutput(String output);

		public abstract TestDetails build();
	}
}
