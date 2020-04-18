package com.exercism.gradle;

import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.tasks.TaskState;

import com.exercism.adapter.ReportAdapter;
import com.exercism.report.ReportGenerator;

public class ExerciseTaskExecutionListener implements TaskExecutionListener {

	public static String TEST = "test";

	@Override
	public void afterExecute(Task task, TaskState state) {
		if(task.getName().equals(TEST)) {
			ReportGenerator.report(ReportAdapter.toReport(ExerciseTestListener.taskDetails));
		}
	}

	@Override
	public void beforeExecute(Task task) {
		
	}

}
