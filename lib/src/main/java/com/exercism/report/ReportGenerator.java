package com.exercism.report;

import java.io.File;

import com.exercism.data.Report;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReportGenerator {
	
	public static void report(Report report) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("results.json"), report);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
