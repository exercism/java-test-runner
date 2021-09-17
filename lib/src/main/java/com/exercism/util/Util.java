package com.exercism.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
	
	public static List<String> formatStacktrace(StackTraceElement[] stack) {
		return Arrays.asList(stack).stream() //
		.map(StackTraceElement::toString) //
		.map(f -> "\n at " + f) //
		.collect(Collectors.toList());
	}

}
