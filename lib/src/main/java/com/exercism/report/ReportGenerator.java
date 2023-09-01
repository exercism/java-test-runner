package com.exercism.report;

import com.exercism.data.Report;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;

public class ReportGenerator {

	private final Path outputDirectory;

	public ReportGenerator(Path outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void report(Report report) {
		var mapper = new ObjectMapper();
		var filePath = this.outputDirectory.resolve("results.json");

		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), report);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
