# Hawk android catcher [![](https://jitpack.io/v/codex-team/hawk.kotlin.svg)](https://jitpack.io/#codex-team/hawk.kotlin)

### Сборщик ошибок

Эта библиотека обеспечивает сбор непроверяемых ошибок во время работы приложения и отправляет их в ваш https://hawk.so
личный кабинет. Так же существует возможность отправлять отловленные в **try-catch** ошибки

-----

### Подключение

#### Maven

Add the JitPack repository to your build file

```
     <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Add the dependency

```
    <dependency>
	    <groupId>com.github.codex-team</groupId>
	    <artifactId>hawk.kotlin</artifactId>
	    <version>v3</version>
	</dependency>
```

#### Gradle

Для подключения библиотеки необходимо добавить в gradle maven репозиторий.

```
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
   }
```

И зависимость на библиотеку

```
   dependencies {
    	implementation 'com.github.codex-team:hawk.kotlin:v1.0.4'
   }
```

### Пример использования

Для активации прослушки ошибок, вы можете добавить в ваш главный класс следующий код

```kotlin
val catcher = HawkCatcher(integrationToken)
    .versionProvider(VersionProviderImpl())
    .userProvider(UserProviderImpl())
    .isDebug(true)
    .build()

catcher.start()
```
**Входные параметры**

> **integrationToken** - уникальный ключ Hawk токен

> **versionProvider** - провайдер для предоставления номера и имени версии приложения

> **userProvider** - провайдер для предоставления уникального идентификатора пользователя и имени пользователя

> **isDebug** - если необходимо отобразить дополнительную информацию

## Пример работы  

Отлавливание **UncheckedException**

```kotlin
fun myTask() {
	var d = 10 / 0;
}

myTask()
```

Отправка отловленных исключений

```kotlin
val catcher: HawkExceptionCatcher

fun myTask() {
    try {
        var d = 10 / 0
    } catch(e: Exception) {
        catcher.caught(e)
        //Данный метод формирует исключение в JSON и отправляет его
    }
}

myTask()
```

При этом ошибки, отловленные в **try-catch** без использования функции **caught()** отправлены не будут

```kotlin
fun myTask() {
    try {
        var d = 10 / 0
    } catch(e: Exception) {
        e.printStackTrace()
        //ошибка отправлена не будет
    }
}

myTask()
```
