package com.exercism.gradle;

import static com.exercism.adapter.TestDetailsAdapter.toTestDetails;
import static com.exercism.gradle.ExercismTestOutputListener.getOutputs;
import static com.exercism.gradle.ExercismTestOutputListener.inbox;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.tasks.testing.TestDescriptor;
import org.gradle.api.tasks.testing.TestListener;
import org.gradle.api.tasks.testing.TestResult;

import com.exercism.data.TestDetails;


public class ExerciseTestListener implements TestListener {
	
	public static List<TestDetails> taskDetails;
	
	public ExerciseTestListener() {
		taskDetails = new ArrayList<TestDetails>();
	}

	public void afterSuite(TestDescriptor suite, TestResult result) {
	}
	
	public void afterTest(TestDescriptor test, TestResult result) {
		taskDetails.add(toTestDetails(test.getName(), result, getOutputs(inbox)));
	}

	public void beforeSuite(TestDescriptor suite) {
    }


    public void beforeTest(TestDescriptor test) {
    }
}
