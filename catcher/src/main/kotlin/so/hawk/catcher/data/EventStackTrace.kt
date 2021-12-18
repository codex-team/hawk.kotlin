package so.hawk.catcher.data

internal data class EventStackTrace(
    val className: String,
    val lineNumber: Int,
    val methodName: String
)