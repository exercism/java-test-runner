package com.exercism.report;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;

public class ReportWriter {

	private final Path outputDirectory;

	public ReportWriter(Path outputDirectory) {
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
