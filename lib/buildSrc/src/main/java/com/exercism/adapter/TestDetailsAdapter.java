package com.exercism.adapter;

import static java.lang.String.join;

import java.util.List;

import org.gradle.api.tasks.testing.TestResult;

import com.exercism.data.TestDetails;
import com.exercism.util.Util;

public class TestDetailsAdapter {

	public static TestDetails toTestDetails(String testName, TestResult result, List<String> outputs) {
		String message = null != result.getException() && null != result.getException().getMessage()//
				? result.getException().getMessage().trim() //
				: "";
		String details = result.getResultType().toString() == "FAILURE" ?
						 result.getException() + Util.formatStacktrace(result.getException().getStackTrace()).toString() : "";
		return TestDetails.builder() //
				.name(testName) //
				.message(message + details) //
				.status(result.getResultType().toString()) //
				.output(join(",", outputs)) //
				.build();

	}
}
