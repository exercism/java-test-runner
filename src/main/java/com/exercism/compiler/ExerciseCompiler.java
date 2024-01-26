package com.exercism.compiler;

import com.google.common.collect.Sets;

import javax.tools.*;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ExerciseCompiler implements Closeable, Flushable {
    private final JavaCompiler compiler;
    private final StandardJavaFileManager fileManager;
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector;

    public ExerciseCompiler(String exerciseSlug) throws IOException {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.diagnosticCollector = new DiagnosticCollector<>();
        this.fileManager = compiler.getStandardFileManager(this.diagnosticCollector, null, null);

        var outputDir = Files.createTempDirectory(String.format("java-test-runner-%s-", exerciseSlug));
        this.fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(outputDir.toFile()));
    }

    public void compile(final Collection<File> filesToCompile) throws ExerciseCompilationException {
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
