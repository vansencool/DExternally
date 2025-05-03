package io.github.vansencool.dexternally;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * A Gradle plugin that provides a task to upload files to a remote server using SSH.
 * <p>
 * This plugin registers the `dexternalUpload` task, which can be used to upload files to a remote server.
 * </p>
 */
@SuppressWarnings("unused")
public class DExternallyPlugin implements Plugin<Project> {

    /**
     * Constructor for the DExternallyPlugin.
     */
    public DExternallyPlugin() {}

    @Override
    public void apply(Project project) {
        project.getTasks().register("dexternalUpload", DExternallyTask.class);
    }
}