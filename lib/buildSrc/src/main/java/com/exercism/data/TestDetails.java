package com.exercism.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDetails {
	private String name;
	private String status;
	private String message;
	private List<String> output;
}
