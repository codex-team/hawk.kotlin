package so.hawk.catcher

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import so.hawk.catcher.addons.Addon
import so.hawk.catcher.addons.UserAddon
import so.hawk.catcher.addons.UserAddonWrapper
import so.hawk.catcher.configurations.HawkConfigurations

class EventHandler(
    private val configurations: HawkConfigurations,
    private val settingProvider: HawkSettingProvider,
    private val userProvider: UserProvider,
    private val gson: Gson,
    private val logger: BaseLogger
) {

    companion object {
        private const val CATCHER_TYPE = "errors/android"
    }

    /**
     * Forming stack trace
     *
     * @param throwable
     * @return
     */
    private fun getStackTrace(throwable: Throwable?): JsonArray {
        val stackTraceElements = throwable!!.stackTrace
        return JsonArray().apply {
            stackTraceElements.map(::convertStackTraceElementToJson)
                .forEach(::add)
        }
    }

    /**
     * Forming stack trace information
     *
     * @param element stack trace element
     * @return json object with information of stack trace element
     */
    private fun convertStackTraceElementToJson(element: StackTraceElement): JsonElement {
        val jsonStackTraceElement = JsonObject()
        jsonStackTraceElement.addProperty("file", element.className)
        jsonStackTraceElement.addProperty("line", element.lineNumber)
        jsonStackTraceElement.addProperty("function", element.methodName)
        return jsonStackTraceElement
    }

    /**
     * Forming payload information of event
     *
     * @param throwable [Throwable]
     * @param isFatal Flag if throwable if fatal
     * @param userAddon Additional external information
     */
    private fun payload(
        throwable: Throwable,
        isFatal: Boolean = true,
        userAddons: List<Addon> = listOf(),
        addons: List<Addon>
    ): JsonElement {
        val versionName = settingProvider.getVersionName()
        val appVersion = settingProvider.getAppVersion()
        val title = throwable.message
        val type = throwable::class.java.simpleName
        val backtrace = getStackTrace(throwable)
        val release = "$versionName($appVersion)"
        val addons = JsonObject().also { addonsObject ->
            addons.forEach { addon ->
                addon.fillJsonObject(addonsObject)
            }
        }
        val context = JsonObject().also { contextObject ->
            userAddons.forEach { addon ->
                addon.fillJsonObject(contextObject)
            }
        }
        val user = userProvider.currentUser()?.let { currentUser ->
            JsonObject().apply {
                addProperty("id", currentUser.id)
                addProperty("name", currentUser.name)
            }
        }
        return JsonObject().apply {
            addProperty("title", title)
            addProperty("type", type)
            add("backtrace", backtrace)
            addProperty("release", release)
            add("addons", addons)
            add("context", context)
            add("user", user)
        }
    }

    /**
     * Create json with exception and device information
     *
     * @param throwable
     * @param isFatal Flag if throwable if fatal
     * @param externalUserAddon Additional external information
     * @return
     */
    fun formingJsonExceptionInfo(
        throwable: Throwable,
        isFatal: Boolean = true,
        externalUserAddon: UserAddon? = null
    ): JsonObject {
        val causeThrowable = throwable.cause ?: throwable
        val reportJson = JsonObject()
        val userAddons = externalUserAddon
            ?.let { configurations.userAddons + UserAddonWrapper(it, gson) }
            ?: configurations.userAddons
        reportJson.addProperty("token", configurations.token)
        reportJson.addProperty("catcherType", CATCHER_TYPE)
        reportJson.add(
            "payload",
            payload(causeThrowable, userAddons = userAddons, addons = configurations.addons)
        )
        logger.i("Post json = $reportJson")
        return reportJson
    }
}