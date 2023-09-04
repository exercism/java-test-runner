package com.exercism;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JavaSourceFileAnalyzer extends VoidVisitorAdapter<Void> {
    private final JavaParser parser;
    private final Set<String> classes;
    private final Map<String, String> methodDeclarations;

    public JavaSourceFileAnalyzer() {
        this.parser = new JavaParser();
        this.classes = new HashSet<>();
        this.methodDeclarations = new HashMap<>();
    }

    public void analyze(File file) throws IOException {
        parser.parse(file).ifSuccessful(compilationUnit -> compilationUnit.accept(this, null));
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);

        this.classes.add(getClassName(n));
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);

        final var parents = new ArrayList<ClassOrInterfaceDeclaration>();
        n.walk(Node.TreeTraversal.PARENTS, node -> {
            if (node instanceof ClassOrInterfaceDeclaration declaration) {
                parents.add(declaration);
            }
        });

        var methodName = getClassName(parents.get(0)) + "." + n.getNameAsString();
        this.methodDeclarations.put(methodName, n.toString());
    }

    private static String getClassName(ClassOrInterfaceDeclaration declaration) {
        return declaration.getFullyQualifiedName().orElse(declaration.getNameAsString());
    }

    public Collection<String> getClasses() {
        return this.classes;
    }

    public Map<String, String> getMethodDeclarations() {
        return this.methodDeclarations;
    }
}
