# Hawk kotlin android catcher
### Сборщик ошибок 
Эта библиотека обеспечивает сбор непроверяемых ошибок во время работы приложения и отправляет их в ваш https://hawk.so личный кабинет.
Так же существует возможность отправлять отловленные в **try-catch** ошибки

-----

Подключение
------------
Добавить в Ваш класс **Application** следующий код

```kotlin

class Start : Application() {
    private var exceptionCatcher: HawkExceptionCatcher? = null
    private fun defineExceptionCather() {
        exceptionCatcher = HawkExceptionCatcher(this, "0927e8cc-f3f0-4ce4-aa27-916f0774af51")
        try {
            exceptionCatcher?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onCreate() {
        super.onCreate()
        defineExceptionCather()
    }
}

```
Входные параметры 


> **Context** - текущий context приложения

> **Token** - уникальный ключ авторизации
Примеры вывода:

```json
{  
   "token":"0927e8cc-f3f0-4ce4-aa27-916f0774af51",
   "message":"java.lang.ArithmeticException: divide by zero",
   "stack":"java.lang.RuntimeException: Unable to start activity ComponentInfo{com.hawkandroidcatcher.akscorp.hawkandroidcatcher\/com.hawkandroidcatcher.akscorp.hawkandroidcatcher.SampleMainActivity}: java.lang.ArithmeticException: divide by zero",
   "brand":"Android",
   "device":"generic_x86",
   "model":"Android SDK built for x86",
   "product":"sdk_google_phone_x86",
   "SDK":"22",
   "release":"5.1.1",
   "incremental":"4212452"
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

> **incremental** - 

## Пример работы  

Отлавливание **UncheckedException**

```kotlin
fun myTask() {
	var d: Integer = 10 / 0
}
...
myTask()
```
Отловленная ошибка будет соотвествовать формату **JSON** выше

Отправка отловленных исключений

```kotlin
fun myTask() {
    try {
        var d: Integer = 10 / 0
    } catch(e: Exception) {
        exceptionCatcher.log(e) 
        //Данный метод формирует исключение в JSON и отправляет его
    }
}
...
myTask()
```
При этом ошибки, отловленные в **try-catch** без использования функции **log()** отправлены не будут

```kotlin
fun myTask() {
    try {
        var d: Integer = 10 / 0
    } catch(e: Exception) {
        e.printStackTrace()
        //ошибка отправлена не будет
    }
}
...
myTask()
```
