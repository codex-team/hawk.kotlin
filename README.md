# Hawk android catcher ![](https://jitpack.io/v/jitpack/maven-simple.svg?style=flat-square)
### Сборщик ошибок 
Эта библиотека обеспечивает сбор непроверяемых ошибок во время работы приложения и отправляет их в ваш https://hawk.so личный кабинет.
Так же существует возможность отправлять отловленные в **try-catch** ошибки

-----

### Подключение
Для подключения библиотеки необходимо добавить в gradle maven репозиторий и ссылку с библиотеку. Пример ниже 
```
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
   }
   ...
   dependencies {
	compile 'com.github.codex-team:hawk.kotlin:master-SNAPSHOT'
   }
```
### Пример использования
Для активации прослушки ошибок, вы можете добавить в ваш класс (например)**Application** следующий код

```kotlin
var exceptionCatcher: HawkExceptionCatcher;
exceptionCatcher = HawkExceptionCatcher(this,"your hawk token");
try {
    exceptionCatcher.start();
} catch (Exception e) {
    e.printStackTrace();
}
```
**Входные параметры** 

> **Context** - текущий context приложения

> **Token** - уникальный ключ авторизации(Например:0927e8cc-f3f0-4ce4-aa27-916f0774af51)

**Примеры вывода:**
```json
{  
   "token":"your hawk token",
   "message":"java.lang.ArithmeticException: divide by zero",
   "stack":"java.lang.RuntimeException: Unable to start activity ComponentInfo{com.hawkandroidcatcher.akscorp.hawkandroidcatcher\/com.hawkandroidcatcher.akscorp.hawkandroidcatcher.SampleMainActivity}: java.lang.ArithmeticException: divide by zero",
   "brand":"Android",
   "device":"generic_x86",
   "model":"Android SDK built for x86",
   "product":"sdk_google_phone_x86",
   "SDK":"22",
   "release":"5.1.1",
}
```

### Параметры вывода
> **message** - название самой ошибки

> **stack** - стек ошибки

> **brand** - код поставщика android устройства

> **device** - имя устройства в рамках индустриального дизайна(?)

> **model** - общеизвестное имя android устройства

> **product** - общее наименование продукции

> **SDK** - версия SDK

> **release** - версия андроида

## Пример работы  

Отлавливание **UncheckedException**

```kotlin
function myTask() {
	var d = 10 / 0;
}
...
myTask();
```
Отловленная ошибка будет соотвествовать формату **JSON** выше

Отправка отловленных исключений

```kotlin
function myTask() {
    try {
        var d = 10 / 0;
    } catch(e: ArithmeticException) {
        exceptionCatcher.log(e); 
        //Данный метод формирует исключение в JSON и отправляет его
    }
}
...
myTask();
```
При этом ошибки, отловленные в **try-catch** без использования функции **log()** отправлены не будут

```kotlin
function myTask() {
    try {
        var d = 10 / 0;
    } catch(e: ArithmeticException) {
        e.printStackTrace();
        //ошибка отправлена не будет
    }
}
...
myTask();
```
