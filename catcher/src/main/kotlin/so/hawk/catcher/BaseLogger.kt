package so.hawk.catcher

class BaseLogger : Logger {
    companion object {
        private val TAG = "HawkCatcher"
        private val WARNING = "WARNING"
    }

    override fun e(message: String, throwable: Throwable?) {
        System.err.println("$TAG $message")
        throwable?.printStackTrace()
    }

    override fun i(message: String) {
        println("$TAG $message")
    }

    override fun w(message: String) {
        println("$TAG [$WARNING] $message")
    }
}