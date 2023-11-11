package me.andrepost.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.sonarqube.gradle.SonarExtension;
import org.sonarqube.gradle.SonarQubePlugin;

import java.util.List;
import java.util.Objects;

public class GradlePlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        applySonarQubePlugin(target);
    }

    private void applySonarQubePlugin(Project target) {
        target.getPluginManager().apply(SonarQubePlugin.class);

        var extension = target.getExtensions().findByType(SonarExtension.class);
        if(Objects.nonNull(extension)) {
            extension.properties(p -> {
                p.property("sonar.host.url", "http://localhost:9000");
                p.property("sonar.projectName", target.getName());
                p.property("sonar.projectKey", target.getGroup() + "." + target.getName().replaceAll("-", "_"));
                p.property("sonar.projectVersion", target.getVersion());
                p.property("sonar.java.coveragePlugin", "jacoco");
                p.property("sonar.java.binaries", "build/classes/java/main");
                p.property("sonar.java.test.binaries", "build/classes/java/test");
                p.property("sonar.coverage.exclusions", List.of("**/Application.java", "**/test/**/*", "**/build/**/*", "**/generated/**/*", "**/configuration/**/*"));
            });
        } else {
            throw new RuntimeException("SonarQube plugin not found");
        }
    }
}
