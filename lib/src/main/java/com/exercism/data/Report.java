package com.exercism.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;

@AutoValue
public abstract class Report {
	public abstract String status();

	public abstract String message();

	public abstract ImmutableList<TestDetails> tests();

	public abstract int version();

	public static Builder builder() {
	  return new AutoValue_Report.Builder();
	}

	@AutoValue.Builder
	public abstract class Builder {
		public abstract Builder setStatus(String status);

		public abstract Builder setMessage(String message);

		public abstract ImmutableList.Builder<TestDetails> countriesBuilder();
		public final Builder addTest(TestDetails test) {
			testsBuilder().add(test);
			return this;
		}

		public abstract Builder setVersion(int version);

		public abstract Report build();
	}
}
