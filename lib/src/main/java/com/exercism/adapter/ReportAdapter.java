package com.exercism.adapter;

import java.util.List;
import java.util.stream.Collectors;

import com.exercism.data.Report;
import com.exercism.data.TestDetails;

public class ReportAdapter {
	public static String ERROR_STATUS = "error";
	public static String PASS_STATUS = "pass";
	public static String FAIL_STATUS = "fail";

	public static Report toReport(List<TestDetails> tests) {
		return Report.builder()
			.setStatus(getStatus(tests))//
			.tests(tests)//
			.build();
	}

	private static String getStatus(List<TestDetails> tests) {
		boolean failedTests = tests.stream().anyMatch(t -> t.getStatus() == "fail");
		return failedTests ? FAIL_STATUS : PASS_STATUS;
	}

	public static Report toReport(String task, List<String> stackTrace) {
		return Report.builder()
			.setStatus(ERROR_STATUS)
			.setMessage("Task: "+ task + ": " + stackTrace.toString())
			.build();
	}
}
