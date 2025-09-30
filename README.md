# Hawk android catcher [![](https://jitpack.io/v/codex-team/hawk.kotlin.svg)](https://jitpack.io/#codex-team/hawk.kotlin)

### Error catcher

This library provides in-app errors catching and sending them to the [Hawk](https://hawk.so) monitoring system. You can also send errors, which you caught in **try-catch**.

-----

### Connection

#### Maven

Add the JitPack repository to your build file

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

Add the dependency

```xml
<dependency>
	<groupId>com.github.codex-team</groupId>
	<artifactId>hawk.kotlin</artifactId>
	<version>v3.0</version>
</dependency>
```

#### Gradle

To connect the library, add the following code to your build.gradle config.

```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And the dependency on the library

```
dependencies {
 	implementation 'com.github.codex-team:hawk.kotlin:v3.0'
}
```

### How to use

To activate the error collector, you can add the following code to your main class

```kotlin
val catcher: HawkExceptionCatcher = HawkCatcher(integrationToken)
    .versionProvider(VersionProviderImpl())
    .userProvider(UserProviderImpl())
    .isDebug(true)
    .build()

catcher.start()
```
**Input parameters**

> **integrationToken** - unique key Hawk token

> **versionProvider** - To provide the application version number and name

> **userProvider** - to provide a unique user ID and user name

> **isDebug** - if you need to display additional information

This example uses the default implementation of the specified providers. For more information you can read about interface [VersionProvider](https://github.com/codex-team/hawk.kotlin/blob/master/catcher/src/main/kotlin/so/hawk/catcher/provider/VersionProvider.kt) and [UserProvider](https://github.com/codex-team/hawk.kotlin/blob/master/catcher/src/main/kotlin/so/hawk/catcher/provider/UserProvider.kt).

## Example

Catching an **UncaughtException**

```kotlin
fun myTask() {
	var d = 10 / 0;
}

myTask()
```

Sending caught exceptions

```kotlin
val catcher: HawkExceptionCatcher

fun myTask() {
    try {
        var d = 10 / 0
    } catch(e: Exception) {
        catcher.caught(e)
        // This method generates an exception in JSON and sends it
    }
}

myTask()
```

At the same time, errors caught in **try-catch** without using the **caught()** function will not be sent

```kotlin
fun myTask() {
    try {
        var d = 10 / 0
    } catch(e: Exception) {
        e.printStackTrace()
        // The error will not be sent
    }
}

myTask()
```

# About CodeX

<img align="right" width="120" height="120" src="https://codex.so/public/app/img/codex-logo.svg" hspace="50">

CodeX is a team of digital specialists around the world interested in building high-quality open source products on a global market. We are [open](https://codex.so/join) for young people who want to constantly improve their skills and grow professionally with experiments in cutting-edge technologies.

| 🌐 | Join  👋  | Twitter | Instagram |
| -- | -- | -- | -- |
| [codex.so](https://codex.so) | [codex.so/join](https://codex.so/join) |[@codex_team](http://twitter.com/codex_team) | [@codex_team](http://instagram.com/codex_team/) |

## License

This project is licensed under the **GNU Affero General Public License v3.0 (AGPL-3.0)**.
See the [LICENSE](./LICENSE) file for the full text.
