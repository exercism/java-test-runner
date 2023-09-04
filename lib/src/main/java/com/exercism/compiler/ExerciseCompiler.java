package com.exercism.compiler;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExerciseCompiler {
    private final String exerciseSlug;

    public ExerciseCompiler(String exerciseSlug) {
        this.exerciseSlug = exerciseSlug;
    }

    public Collection<Class<?>> compile(final Collection<File> filesToCompile, final Collection<String> classesToLoad) throws ExerciseCompilationException, IOException {
        var compiler = ToolProvider.getSystemJavaCompiler();
        var diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
        var outputDir = Files.createTempDirectory(String.format("java-test-runner-%s-", this.exerciseSlug));
        try (var fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null)) {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(outputDir.toFile()));
            var sources = fileManager.getJavaFileObjectsFromFiles(filesToCompile);
            var task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, sources);
            if (!task.call()) {
                var messageBuilder = new StringBuilder();
                for (var diagnostics : diagnosticCollector.getDiagnostics()) {
                    messageBuilder.append(diagnostics.toString());
                }
                throw new ExerciseCompilationException(messageBuilder.toString());
            }

            var loader = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
            var javaFileObjects = fileManager.list(StandardLocation.CLASS_OUTPUT, "", Collections.singleton(JavaFileObject.Kind.CLASS), true);
            var classes = new ArrayList<Class<?>>();

            for (String className : classesToLoad) {
                System.out.println("Loading class: " + className);
                classes.add(loader.loadClass(className));
            }

            return classes;
        } catch (ClassNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}
