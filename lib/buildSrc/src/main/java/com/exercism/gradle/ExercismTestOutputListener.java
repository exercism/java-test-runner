package com.exercism.gradle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.gradle.api.tasks.testing.TestDescriptor;
import org.gradle.api.tasks.testing.TestOutputEvent;
import org.gradle.api.tasks.testing.TestOutputListener;

public class ExercismTestOutputListener implements TestOutputListener{
	
	private static final int INBOX_CAPACITY = 50 ;
	private static final boolean THREAD_ACCESS_FAIRNESS = true; // FIFO
	
	public static ArrayBlockingQueue<String> inbox;
	
	public ExercismTestOutputListener() {
		inbox = new ArrayBlockingQueue<>(INBOX_CAPACITY, THREAD_ACCESS_FAIRNESS);
	}
	
	@Override
	public void onOutput(TestDescriptor discripter, TestOutputEvent event) {
		inbox.offer(event.getMessage());
	}
	
	public static List<String> getOutputs(ArrayBlockingQueue<String> inbox) {
		List<String> outputs = new ArrayList<String>();
		
		String standardOutputs;
		while((standardOutputs= inbox.poll()) != null) {
			outputs.add(standardOutputs);
		};
		return outputs;
	}

}
