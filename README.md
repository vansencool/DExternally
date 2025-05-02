# DExternally

**A simple, fast-enough Gradle plugin to upload your built JAR files via SFTP.**

---

## 💡 What it does

DExternally provides a customizable Gradle task (`dexternallyUpload`) to upload a compiled `.jar` file to a remote server via SFTP.

---

## Configuration

You can configure the task via properties inside your `build.gradle`.

### Available properties

| Property          | Type     | Required | Default  | Description                                  |
|------------------|----------|----------|----------|----------------------------------------------|
| `host`           | `String` | ✅       | —        | Remote host IP or domain                     |
| `user`           | `String` | ✅       | —        | Username                                     |
| `password`       | `String` | ✅       | —        | Password                                     |
| `remote`         | `String` | ✅       | —        | Full remote directory path (no trailing `/`) |
| `jarFile`        | `File`   | ✅       | —        | JAR file to upload                           |
| `port`           | `Int`    | ❌       | `22`     | Port                                         |
| `compression`    | `Bool`   | ❌       | `false`  | Enable SSH compression                       |
| `sshTimeout`     | `Int`    | ❌       | `10000`  | Timeout for SSH in milliseconds              |
| `connectTimeout` | `Int`    | ❌       | `10000`  | Timeout for connection in milliseconds       |

---

## Example usage

### `build.gradle` (Groovy DSL)

```groovy
plugins {
    id 'java'
    id 'net.vansen.dexternally' version '1.0.0'
}

dexternallyUpload {
    host = "example.com"
    user = "root"
    password = "your_password"
    port = 2222
    remote = "/path/to/directory"
    jarFile = file("build/libs/your-plugin.jar")
}
