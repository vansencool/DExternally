# DExternally

**A simple Gradle plugin to upload your built files via SFTP.**

---

## What it does

DExternally provides a customizable Gradle task (`dexternallyUpload`) to upload one or more compiled files (e.g., JARs) to a remote server via SFTP.

---

## Configuration

You can configure the task via properties inside your gradle build.

### Available properties

| Property         | Type         | Required | Default | Description                                  |
| ---------------- |--------------| -------- |---------|----------------------------------------------|
| `host`           | `String`     | ✅        | —       | Remote host IP or domain                     |
| `user`           | `String`     | ✅        | —       | Username                                     |
| `password`       | `String`     | ✅        | —       | Password                                     |
| `files`          | `List<File>` | ✅        | —       | List of files to upload                      |
| `remote`         | `String`     | ❌        | `/`     | Remote directory path (defaults to root dir) |
| `port`           | `Integer`    | ❌        | `22`    | Port                                         |
| `compression`    | `Boolean`    | ❌        | `false` | Enable SSH compression                       |
| `sshTimeout`     | `Integer`    | ❌        | `10000` | Timeout for SSH in milliseconds              |
| `connectTimeout` | `Integer`    | ❌        | `10000` | Timeout for connection in milliseconds       |

---

## Example usage

### `build.gradle` (Groovy DSL)

```groovy
plugins {
    id 'java'
    id 'net.vansen.dexternally' version '1.0.1'
}

dexternallyUpload {
    host = "example.com"
    user = "root"
    password = "your_password"
    port = 2222
    remote = "/apps/myapp"
    files = List.of("build/libs/MyApp-1.0.jar")
}
```

---

### Example with Shadow JAR

```groovy
dexternallyUpload {
    dependsOn tasks.shadowJar
    host = "example.com"
    user = "root"
    password = "your_password"
    files = List.of(tasks.shadowJar.archiveFile.get().asFile) // upload built (shadow) jar
}
```

---