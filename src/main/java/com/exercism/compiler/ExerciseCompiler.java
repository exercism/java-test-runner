package com.exercism.compiler;

import com.google.common.collect.Sets;

import javax.tools.*;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ExerciseCompiler implements Closeable, Flushable {
    // The native javac is built when the Docker image is built (see bin/build-native-javac.sh).
    // It compiles against the Java API of the release given here, read from lib/ct.sym.
    private static final Path NATIVE_JAVAC_HOME = Path.of("/opt/test-runner/native-javac");
    private static final Path NATIVE_JAVAC = NATIVE_JAVAC_HOME.resolve("javac");
    private static final String NATIVE_JAVAC_RELEASE = "25";

    private final JavaCompiler compiler;
    private final StandardJavaFileManager fileManager;
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector;
    private final Path outputDir;
    private final Collection<File> filesToCompile;
    private final Process nativeCompilation;

    public ExerciseCompiler(String exerciseSlug, Collection<File> filesToCompile) throws IOException {
        this.filesToCompile = filesToCompile;
        this.outputDir = Files.createTempDirectory(String.format("java-test-runner-%s-", exerciseSlug));
        this.nativeCompilation = startNativeCompilation();

        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.diagnosticCollector = new DiagnosticCollector<>();
        this.fileManager = compiler.getStandardFileManager(this.diagnosticCollector, null, null);
        this.fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(outputDir.toFile()));
    }

    // The native javac starts compiling straight away, in its own process, while the
    // test runner parses the test files. If it fails for any reason, compile() will
    // compile the files again with the JVM's own javac. That keeps compile errors
    // exactly as before, and handles code that needs a newer Java release.
    public void compile() throws ExerciseCompilationException {
        if (nativeCompilationSucceeded()) {
            return;
        }

        var sources = fileManager.getJavaFileObjectsFromFiles(filesToCompile);
        var task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, sources);
        if (task.call()) {
            return;
        }

        var messageBuilder = new StringBuilder();
        for (var diagnostics : diagnosticCollector.getDiagnostics()) {
            messageBuilder.append(diagnostics.toString());
        }
        throw new ExerciseCompilationException(messageBuilder.toString());
    }

    private Process startNativeCompilation() {
        if (!Files.isExecutable(NATIVE_JAVAC)) {
            return null;
        }

        var command = new ArrayList<>(List.of(
                NATIVE_JAVAC.toString(),
                "-Djava.home=" + NATIVE_JAVAC_HOME,
                "--release", NATIVE_JAVAC_RELEASE,
                "-classpath", System.getProperty("java.class.path"),
                "-d", outputDir.toString()));
        for (var file : filesToCompile) {
            command.add(file.getPath());
        }

        try {
            return new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                    .start();
        } catch (IOException e) {
            return null;
        }
    }

    private boolean nativeCompilationSucceeded() {
        if (nativeCompilation == null) {
            return false;
        }

        try {
            return nativeCompilation.waitFor() == 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public ClassLoader getClassLoader() {
        return this.fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
    }

    public Set<Path> getClasspathRoots() {
        return Sets.newHashSet(this.fileManager.getLocationAsPaths(StandardLocation.CLASS_OUTPUT));
    }

    @Override
    public void close() throws IOException {
        this.fileManager.close();
    }

    @Override
    public void flush() throws IOException {
        this.fileManager.flush();
    }
}
