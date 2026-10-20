package org.gradle.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Minimal, auditable Gradle launcher for the supplied Android Studio project.
 * Source is retained here. It fetches the version pinned by gradle-wrapper.properties.
 * Replace this bootstrap jar with the official Gradle wrapper via `gradle wrapper`
 * after your first successful sync if desired.
 */
public final class GradleWrapperMain {
    public static void main(String[] args) throws Exception {
        Path base = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        Path config = base.resolve("gradle/wrapper/gradle-wrapper.properties");
        if (!Files.exists(config)) throw new IOException("Missing " + config);
        Properties p = new Properties();
        try (InputStream input = Files.newInputStream(config)) { p.load(input); }
        String distributionUrl = p.getProperty("distributionUrl");
        if (distributionUrl == null || !distributionUrl.startsWith("https://services.gradle.org/distributions/gradle-")) {
            throw new IOException("Unexpected distribution URL: " + distributionUrl);
        }
        String fileName = distributionUrl.substring(distributionUrl.lastIndexOf('/') + 1);
        String version = fileName.replace("-bin.zip", "").replace("-all.zip", "");
        String gradleHomeValue = System.getenv("GRADLE_USER_HOME");
        Path cache = (gradleHomeValue != null && !gradleHomeValue.isBlank())
            ? Paths.get(gradleHomeValue) : Paths.get(System.getProperty("user.home"), ".gradle");
        Path root = cache.resolve("wrapper/dists/dalawingo");
        Path install = root.resolve(version);
        Path launcher = install.resolve("lib/gradle-launcher-" + version.replace("gradle-", "") + ".jar");
        if (!Files.isRegularFile(launcher)) {
            Files.createDirectories(root);
            Path zip = root.resolve(fileName);
            if (!Files.isRegularFile(zip)) {
                System.out.println("Downloading pinned Gradle distribution: " + distributionUrl);
                HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(30)).build();
                HttpRequest request = HttpRequest.newBuilder(URI.create(distributionUrl))
                    .timeout(Duration.ofMinutes(8)).GET().build();
                HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(zip));
                if (response.statusCode() != 200) {
                    Files.deleteIfExists(zip);
                    throw new IOException("Gradle download returned HTTP " + response.statusCode());
                }
            }
            System.out.println("Extracting Gradle distribution...");
            try (ZipInputStream stream = new ZipInputStream(Files.newInputStream(zip))) {
                ZipEntry entry;
                while ((entry = stream.getNextEntry()) != null) {
                    Path destination = root.resolve(entry.getName()).normalize();
                    if (!destination.startsWith(root)) throw new IOException("Unsafe Gradle zip entry");
                    if (entry.isDirectory()) Files.createDirectories(destination);
                    else {
                        Files.createDirectories(destination.getParent());
                        Files.copy(stream, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            if (!Files.isRegularFile(launcher)) throw new IOException("Gradle launcher not found after extraction: " + launcher);
        }
        List<String> cmd = new ArrayList<>();
        String java = Paths.get(System.getProperty("java.home"), "bin",
            System.getProperty("os.name").toLowerCase().contains("win") ? "java.exe" : "java").toString();
        cmd.add(java);
        cmd.add("-Dorg.gradle.appname=gradlew");
        cmd.add("-classpath");
        cmd.add(launcher.toString());
        cmd.add("org.gradle.launcher.GradleMain");
        cmd.addAll(List.of(args));
        Process child = new ProcessBuilder(cmd).directory(base.toFile()).inheritIO().start();
        System.exit(child.waitFor());
    }
}
