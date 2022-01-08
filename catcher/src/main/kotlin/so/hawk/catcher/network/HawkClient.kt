package so.hawk.catcher.network

import so.hawk.catcher.Logger
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

/**
 * Client that prepare connection and sending to [url] with current integrationId
 * @param integrationId part of url
 */
class HawkClient(
    integrationId: String,
    private val logger: Logger
) {
    companion object {
        /**
         * Request method
         */
        private const val REQUEST_METHOD = "POST"

        /**
         * Property key for content type
         */
        private const val PROPERTY_CONTENT_TYPE_KEY = "Content-Type"

        /**
         * Property value for content type
         */
        private const val PROPERTY_CONTENT_TYPE_VALUE = "application/json;charset=UTF-8"

        /**
         * Maximum time of connection with server in milliseconds
         */
        const val DEFAULT_TIMEOUT_OF_CONNECTION_IN_MILLISECONDS = 10000L
    }

    /**
     * URL that sending events
     */
    private val url = URL("https://${integrationId}.k1.hawk.so/")

    /**
     * Used to wait for an event to be sent and receive a response
     */
    private val countDownLatch = CountDownLatch(1)

    /**
     * Prepare connection. Setup request method, properties and set connection that can be used output and input
     * @param connection url connection that apply current settings
     */
    private fun HttpsURLConnection.prepareConnection() {
        requestMethod = REQUEST_METHOD
        setRequestProperty(PROPERTY_CONTENT_TYPE_KEY, PROPERTY_CONTENT_TYPE_VALUE)
        doOutput = true
        doInput = true
    }

    /**
     * Send Event by URL
     * @param eventJson contains all information about Event
     */
    fun sendEvent(eventJson: String) {
        logger.i("connection to $url start ${System.currentTimeMillis()}")
        val connection = url.openConnection() as HttpsURLConnection

        connection.prepareConnection()

        connection.outputStream.bufferedWriter().use {
            it.write(eventJson)
        }

        logger.i("connection response status ${connection.responseCode}")
        logger.i("connection response message ${connection.responseMessage}")
        countDownLatch.countDown()
    }

    /**
     * Waiting for the event to go
     */
    fun await() {
        countDownLatch.await(DEFAULT_TIMEOUT_OF_CONNECTION_IN_MILLISECONDS, TimeUnit.MILLISECONDS)
    }

}