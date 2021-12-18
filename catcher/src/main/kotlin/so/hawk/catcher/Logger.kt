package so.hawk.catcher

interface Logger {
    fun e(message: String, throwable: Throwable? = null)

    fun i(message: String)

    fun w(message: String)
}