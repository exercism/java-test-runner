package com.exercism;

import java.io.IOException;

public class Restore {

	public static void main(String[] args) throws IOException {
		new TestRunner(args[0], args[1], args[2]).run();
	}

}
