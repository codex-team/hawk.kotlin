package so.hawk.catcher

interface Logger {
    /**
     * Showing errors
     * @param message Show message of errors
     * @param throwable The exception that was caught
     */
    fun e(message: String, throwable: Throwable? = null)

    /**
     * Show additional information
     * @param message Text
     */
    fun i(message: String)

    /**
     * Show warning messages
     * @param message Text
     */
    fun w(message: String)
}