#!/usr/bin/env sh

# Synopsis:
# Build javac as a native executable with GraalVM Native Image.
# This runs once, in the native-javac stage of the Docker build.

# Output:
# Writes /native-javac/javac and /native-javac/lib/ct.sym.
# The Dockerfile copies them to /opt/test-runner/native-javac.

# A native javac starts in a few milliseconds and runs as compiled machine code from
# the start. The javac inside the JVM has to be loaded and warmed up by the JIT on
# every run, which takes most of a second.
#
# A native executable can't load JDK classes from lib/modules, so the native javac
# compiles against the class signatures in lib/ct.sym, selected with --release.
# ExerciseCompiler passes the same --release when it runs the native javac.

set -e

release=25
config_folder="/tmp/native-javac-config"
classes_folder="/tmp/native-javac-classes"

# javac looks up some classes and resources by name. GraalVM's tracing agent records
# them while javac compiles the sample exercise, so the native executable includes them.
mkdir -p "${classes_folder}" /native-javac
java -agentlib:native-image-agent=config-output-dir="${config_folder}" \
     -m jdk.compiler/com.sun.tools.javac.Main \
     --release "${release}" \
     -classpath /tmp/java-test-runner.jar \
     -d "${classes_folder}" \
     $(find /tmp/aot-training -name '*.java')

native-image --no-fallback \
     -H:+UnlockExperimentalVMOptions \
     -H:ConfigurationFileDirectories="${config_folder}" \
     -H:IncludeResourceBundles=com.sun.tools.javac.resources.javac,com.sun.tools.javac.resources.compiler,com.sun.tools.javac.resources.ct \
     -m jdk.compiler/com.sun.tools.javac.Main \
     -o /native-javac/javac

mkdir -p /native-javac/lib
cp "${JAVA_HOME}/lib/ct.sym" /native-javac/lib/ct.sym
