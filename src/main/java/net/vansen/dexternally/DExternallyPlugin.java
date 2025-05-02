package net.vansen.dexternally;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DExternallyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().register("dexternalUpload", DExternallyTask.class);
    }
}