package io.github.vansencool.dexternally;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.*;

import java.io.File;

/**
 * A Gradle task that uploads a JAR file to a remote server using SSH.
 * <p>
 * This task uses the SSHJ library.
 * </p>
 */
@SuppressWarnings("unused")
public abstract class DExternallyTask extends DefaultTask {

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
     * The remote directory where the file will be uploaded.
     */
    @Input
    public String remote;

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
     * The JAR file to be uploaded.
     */
    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File jarFile;

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getRemote() {
        return remote;
    }

    public Integer getPort() {
        return port;
    }

    public File getJarFile() {
        return jarFile;
    }

    public Boolean getCompression() {
        return compression;
    }

    public Integer getSshTimeout() {
        return sshTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    @TaskAction
    public void upload() {
        if (!jarFile.exists()) {
            throw new IllegalStateException("JAR file not found: " + jarFile.getAbsolutePath());
        }

        try (SSHClient ssh = new SSHClient()) {
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(host, port);
            ssh.authPassword(user, password);
            if (compression) {
                ssh.useCompression();
            }
            ssh.setTimeout(sshTimeout);
            ssh.setConnectTimeout(connectTimeout);

            try (SFTPClient sftp = ssh.newSFTPClient()) {
                sftp.put(jarFile.getAbsolutePath(), remote + "/" + jarFile.getName());
            }

            ssh.disconnect();
            getLogger().lifecycle("[DExternally] Uploaded: " + jarFile.getName());
        } catch (Exception e) {
            throw new RuntimeException("[DExternally] SFTP upload failed", e);
        }
    }
}