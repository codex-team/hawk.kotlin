package so.hawk.catcher

import com.google.gson.Gson
import com.google.gson.JsonElement
import so.hawk.catcher.addons.CustomAddon
import so.hawk.catcher.configurations.HawkConfigurations
import so.hawk.catcher.network.HawkClient
import so.hawk.catcher.provider.UserProvider
import so.hawk.catcher.provider.VersionProvider
import kotlin.system.exitProcess

/**
 * Main class of Hawk Catcher.
 */
class HawkExceptionCatcher(
    token: String,
    versionProvider: VersionProvider,
    userProvider: UserProvider,
    isDebug: Boolean
) : Thread.UncaughtExceptionHandler {

    /**
     * Old handler that will setup like as default in application
     */
    private var oldHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * Return current catcher status
     *
     * @return
     */
    var isActive = false
        private set

    /**
     * Meta data provider for getting information
     */
    private val hawkSettingProvider: HawkSettingProvider =
        DefaultSettingProvider(token, versionProvider)

    /**
     * Used for boxing events to json objects
     */
    private val gson = Gson()

    /**
     * Show information, warning or errors
     */
    private val logger = BaseLogger(isDebug)

    /**
     * Contains common configuration for running Hawk Catcher
     */
    private val configuration: HawkConfigurations = HawkConfigurations(
        hawkSettingProvider.token,
        listOf(),
        gson = gson,
        tokenParser = TokenParserImpl(),
        logger = logger
    )

    /**
     * Event handler with boxing to json
     */
    private val eventHandler: EventHandler =
        EventHandler(configuration, settingProvider = hawkSettingProvider, userProvider, gson, logger)

    /**
     * Client for sending events
     */
    private var client: HawkClient? = null

    /**
     * Start listen uncaught exceptions
     *
     */
    fun start() {
        if (!configuration.isCorrect) {
            logger.e("Cannot start HawkCatcher with incorrect token. Cant get integrationId")
            return
        }
        isActive = true
        client = HawkClient(configuration.integrationId, logger)
        oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * Add custom additional addons
     * @param customAddon custom addon
     */
    fun addCustomAddon(customAddon: CustomAddon) {
        configuration.addCustomAddon(customAddon)
    }

    /**
     * Remove custom additional addons by instance
     * @param customAddon instance of previously added [CustomAddon]
     */
    fun removeCustomAddon(customAddon: CustomAddon) {
        configuration.removeCustomAddon(customAddon)
    }

    /**
     * Remove custom additional addon by name
     * @param name name of [CustomAddon]. See in [CustomAddon.name]
     */
    fun removeCustomAddonByName(name: String) {
        configuration.removeCustomAddon(name)
    }

    /**
     * Stop listen uncaught exceptions and set uncaught exception handler by default
     */
    fun finish() {
        if (!isActive) {
            logger.w("HawkExceptionCatcher not running")
        }
        isActive = false
        Thread.setDefaultUncaughtExceptionHandler(oldHandler)
    }

    /**
     * Action when exception catch
     *
     * @param thread
     * @param throwable
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val uncaughtExceptionJson = eventHandler.formingJsonExceptionInfo(throwable)

        startExceptionPostService(uncaughtExceptionJson)
        oldHandler?.uncaughtException(thread, throwable) ?: run {
            println(
                "Exception in thread \"${thread.name}\" "
            )
            throwable.printStackTrace(System.err)
            exitProcess(1)
        }
    }

    /**
     * Post any exception to server
     *
     * @param throwable
     */
    fun caught(throwable: Throwable) {
        startExceptionPostService(eventHandler.formingJsonExceptionInfo(throwable, isFatal = false))
    }

    /**
     * Post any exception to server
     *
     * @param throwable
     * @param map
     */
    fun caught(throwable: Throwable, map: Map<String, Any>) {
        startExceptionPostService(
            eventHandler.formingJsonExceptionInfo(
                throwable, isFatal = false,
                object : CustomAddon {
                    override val name: String
                        get() = "customData"

                    override fun provideData(): Map<String, Any> = map
                }
            )
        )
    }

    /**
     * Post any exception to server
     *
     * @param throwable
     * @param customCustomAddon
     */
    fun caught(throwable: Throwable, customCustomAddon: CustomAddon) {
        startExceptionPostService(
            eventHandler.formingJsonExceptionInfo(
                throwable,
                isFatal = false,
                customCustomAddon
            )
        )
    }

    /**
     * Start service with post data
     *
     * @param exceptionInfoJSON
     * @param withWaiting
     */
    private fun startExceptionPostService(
        exceptionInfoJSON: JsonElement,
        withWaiting: Boolean = true
    ) {
        val hawkClient = client
        if (hawkClient == null) {
            logger.e("Cant send event without correct a client")
            return
        }
        val json = gson.toJson(exceptionInfoJSON)
        PostExecutionService(hawkClient)
            .postEvent(json.toString())
        if (withWaiting) {
            hawkClient.await()
        }
    }
}