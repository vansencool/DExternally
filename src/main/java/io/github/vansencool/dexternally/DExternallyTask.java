package io.github.vansencool.dexternally;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.*;

import java.io.File;
import java.util.List;

/**
 * A Gradle task that uploads files to a remote server.
 */
@SuppressWarnings("unused")
public abstract class DExternallyTask extends DefaultTask {

    /**
     * Constructor for the DExternallyTask.
     */
    public DExternallyTask() {
        setGroup("DExternally");
        setDescription("Upload files to a remote server using SSH.");
    }

    /**
     * The host of the remote server.
     */
    @Input
    public String host;

    /**
     * The user for SSH authentication.
     */
    @Input
    public String user;

    /**
     * The password for SSH authentication.
     */
    @Input
    public String password;

    /**
     * The remote directory where the file(s) will be uploaded.
     * Defaults to the root directory if not set.
     */
    @Input
    @Optional
    public String remote = "/";

    /**
     * The port for SSH connection.
     */
    @Input
    @Optional
    public Integer port = 22;

    /**
     * Whether to use compression for the SSH connection.
     */
    @Input
    @Optional
    public Boolean compression = false;

    /**
     * The timeout for the SSH connection in milliseconds.
     */
    @Input
    @Optional
    public Integer sshTimeout = 10000;

    /**
     * The timeout for the connection in milliseconds.
     */
    @Input
    @Optional
    public Integer connectTimeout = 10000;

    /**
     * The files to be uploaded.
     */
    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    public List<File> files;

    /**
     * Gets the host of the remote server.
     *
     * @return the host of the remote server
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the user for SSH authentication.
     *
     * @return the user for SSH authentication
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the password for SSH authentication.
     *
     * @return the password for SSH authentication
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the remote directory where the file(s) will be uploaded.
     *
     * @return the remote directory
     */
    public String getRemote() {
        return remote;
    }

    /**
     * Gets the port for SSH connection.
     *
     * @return the port for SSH connection
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Gets the files to be uploaded.
     *
     * @return the files to be uploaded
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Gets whether to use compression for the SSH connection.
     *
     * @return true if compression is enabled, false otherwise
     */
    public Boolean getCompression() {
        return compression;
    }

    /**
     * Gets the timeout for the SSH connection in milliseconds.
     *
     * @return the timeout for the SSH connection
     */
    public Integer getSshTimeout() {
        return sshTimeout;
    }

    /**
     * Gets the timeout for the connection in milliseconds.
     *
     * @return the timeout for the connection
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Uploads the specified files to the remote server.
     */
    @TaskAction
    public void upload() {
        if (files == null || files.isEmpty()) {
            throw new IllegalStateException("No files specified for upload.");
        }

        try (SSHClient ssh = new SSHClient()) {
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(host, port);
            ssh.authPassword(user, password);
            if (compression) ssh.useCompression();
            ssh.setTimeout(sshTimeout);
            ssh.setConnectTimeout(connectTimeout);

            try (SFTPClient sftp = ssh.newSFTPClient()) {
                for (File f : files) {
                    if (!f.exists()) {
                        getLogger().warn("[DExternally] File not found, skipping: {}", f.getAbsolutePath());
                        continue;
                    }
                    sftp.put(f.getAbsolutePath(), remote + "/" + f.getName());
                    getLogger().lifecycle("[DExternally] Uploaded: " + f.getName());
                }
            }

            ssh.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("[DExternally] SFTP upload failed", e);
        }
    }
}