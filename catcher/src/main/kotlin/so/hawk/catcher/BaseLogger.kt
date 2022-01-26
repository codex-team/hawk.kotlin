package so.hawk.catcher

/**
 * Base logger for additional information if [HawkCatcher._isDebug] is enabled or show warning and errors in console
 * @param isDebug Flag for show additional logs
 */
internal class BaseLogger(
    private val isDebug: Boolean
) : Logger {
    companion object {
        /**
         * Tag with showing logs
         */
        private const val TAG = "HawkCatcher"

        /**
         * Text with warning messages
         */
        private const val WARNING = "WARNING"
    }

    /**
     * Showing errors
     * @param message Show message of errors
     * @param throwable The exception that was caught
     */
    override fun e(message: String, throwable: Throwable?) {
        System.err.println("$TAG $message")
        throwable?.printStackTrace()
    }

    /**
     * Show additional information
     * @param message Text
     */
    override fun i(message: String) {
        if (isDebug) {
            println("$TAG $message")
        }
    }

    /**
     * Show warning messages
     * @param message Text
     */
    override fun w(message: String) {
        println("$TAG [$WARNING] $message")
    }
}