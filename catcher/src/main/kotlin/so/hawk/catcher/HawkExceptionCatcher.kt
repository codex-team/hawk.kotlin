package so.hawk.catcher

import com.google.gson.Gson
import com.google.gson.JsonElement
import so.hawk.catcher.addons.UserAddon
import so.hawk.catcher.configurations.HawkConfigurations
import so.hawk.catcher.network.HawkClient
import kotlin.system.exitProcess

/**
 * Main class of Hawk Catcher.
 */
class HawkExceptionCatcher(
    token: String,
    versionProvider: VersionProvider,
    userProvider: UserProvider
) :
    Thread.UncaughtExceptionHandler {

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
    private val metaDataProvider: HawkSettingProvider =
        DefaultSettingProvider(token, versionProvider)

    private val gson = Gson()

    private val logger = BaseLogger()

    /**
     * Contains common configuration for running Hawk Catcher
     */
    private val configuration: HawkConfigurations = HawkConfigurations(
        metaDataProvider.getToken(),
        listOf(),
        gson = gson,
        tokenParser = TokenParserImpl(gson),
        logger = logger
    )

    private val eventHandler: EventHandler =
        EventHandler(configuration, settingProvider = metaDataProvider, userProvider, gson, logger)

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

    fun addUserAddon(userAddon: UserAddon) {
        configuration.addUserAddon(userAddon)
    }

    fun removeUserAddon(userAddon: UserAddon) {
        configuration.removeUserAddon(userAddon)
    }

    fun removeUserAddonByName(name: String) {
        configuration.removeUserAddon(name)
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
                object : UserAddon {
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
     * @param customUserAddon
     */
    fun caught(throwable: Throwable, customUserAddon: UserAddon) {
        startExceptionPostService(
            eventHandler.formingJsonExceptionInfo(
                throwable,
                isFatal = false,
                customUserAddon
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