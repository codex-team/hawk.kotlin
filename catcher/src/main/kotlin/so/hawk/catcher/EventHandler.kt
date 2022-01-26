package so.hawk.catcher

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import so.hawk.catcher.addons.Addon
import so.hawk.catcher.addons.CustomAddon
import so.hawk.catcher.addons.CustomAddonWrapper
import so.hawk.catcher.configurations.IConfigurations
import so.hawk.catcher.provider.UserProvider

/**
 * Events are processed and collected into Json objects
 * @param configurations Contains the basic settings of catcher operation
 * @param settingProvider Contains information about the application and the token
 * @param userProvider Contains information about user
 * @param gson To build Json objects
 * @param logger Logging events when building and sending json objects
 */
internal class EventHandler(
    private val configurations: IConfigurations,
    private val settingProvider: HawkSettingProvider,
    private val userProvider: UserProvider,
    private val gson: Gson,
    private val logger: BaseLogger
) {

    companion object {
        private const val CATCHER_TYPE = "errors/android"

        /**
         * List of keys for building stacktrace json
         */
        private const val STACKTRACE_FILE_KEY = "file"
        private const val STACKTRACE_LINE_KEY = "line"
        private const val STACKTRACE_FUNCTION_KEY = "function"

        /**
         * List of keys for building user json
         */
        private const val USER_ID_KEY = "id"
        private const val USER_NAME_KEY = "name"

        /**
         * List of keys for building payload json
         */
        private const val PAYLOAD_TITLE_KEY = "title"
        private const val PAYLOAD_TYPE_KEY = "type"
        private const val PAYLOAD_BACKTRACE_KEY = "backtrace"
        private const val PAYLOAD_RELEASE_KEY = "release"
        private const val PAYLOAD_ADDONS_KEY = "addons"
        private const val PAYLOAD_CONTEXT_KEY = "context"
        private const val PAYLOAD_USER_KEY = "user"

        /**
         * List of keys for building json
         */
        private const val TOKEN_KEY = "token"
        private const val CATCHER_TYPE_KEY = "catcherType"
        private const val PAYLOAD_KEY = "payload"
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
        jsonStackTraceElement.addProperty(STACKTRACE_FILE_KEY, element.className)
        jsonStackTraceElement.addProperty(STACKTRACE_LINE_KEY, element.lineNumber)
        jsonStackTraceElement.addProperty(STACKTRACE_FUNCTION_KEY, element.methodName)
        return jsonStackTraceElement
    }

    /**
     * Forming payload information of event
     *
     * @param throwable [Throwable]
     * @param isFatal Flag if throwable is fatal
     * @param customAddons Additional external information
     * @param defaultAddons List of additional addons for adding information about the event
     * @return An object that contains information about the error, about the user, information about the application
     * and additional information
     */
    private fun payload(
        throwable: Throwable,
        isFatal: Boolean = true,
        customAddons: List<Addon> = listOf(),
        defaultAddons: List<Addon>
    ): JsonElement {
        val versionName = settingProvider.versionName
        val appVersion = settingProvider.appVersion
        val title = throwable.message
        val type = throwable::class.java.simpleName
        val backtrace = getStackTrace(throwable)
        val release = "$versionName($appVersion)"
        val addons = JsonObject().also { addonsObject ->
            defaultAddons.forEach { addon ->
                addon.fillJsonObject(addonsObject)
            }
        }
        val context = JsonObject().also { contextObject ->
            customAddons.forEach { addon ->
                addon.fillJsonObject(contextObject)
            }
        }
        val user = userProvider.currentUser()?.let { currentUser ->
            JsonObject().apply {
                addProperty(USER_ID_KEY, currentUser.id)
                addProperty(USER_NAME_KEY, currentUser.name)
            }
        }
        return JsonObject().apply {
            addProperty(PAYLOAD_TITLE_KEY, title)
            addProperty(PAYLOAD_TYPE_KEY, type)
            add(PAYLOAD_BACKTRACE_KEY, backtrace)
            addProperty(PAYLOAD_RELEASE_KEY, release)
            add(PAYLOAD_ADDONS_KEY, addons)
            add(PAYLOAD_CONTEXT_KEY, context)
            add(PAYLOAD_USER_KEY, user)
        }
    }

    /**
     * Create json with exception and device information
     * @param throwable Event to be processed
     * @param isFatal Flag if throwable is fatal
     * @param externalCustomAddon Additional external information
     * @return A json object that represents a handled exception with additional information
     */
    fun formingJsonExceptionInfo(
        throwable: Throwable,
        isFatal: Boolean = true,
        externalCustomAddon: CustomAddon? = null
    ): JsonObject {
        val causeThrowable = throwable.cause ?: throwable
        val reportJson = JsonObject()
        val customAddons = externalCustomAddon
            ?.let { configurations.customAddons + CustomAddonWrapper(it, gson) }
            ?: configurations.customAddons
        reportJson.addProperty(TOKEN_KEY, settingProvider.token)
        reportJson.addProperty(CATCHER_TYPE_KEY, CATCHER_TYPE)
        reportJson.add(
            PAYLOAD_KEY,
            payload(causeThrowable, customAddons = customAddons, defaultAddons = configurations.addons)
        )
        logger.i("Post json = $reportJson")
        return reportJson
    }
}