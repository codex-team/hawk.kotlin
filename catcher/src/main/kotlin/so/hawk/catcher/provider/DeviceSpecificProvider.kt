package so.hawk.catcher.provider

import com.google.gson.JsonObject

open class DeviceSpecificProvider {
    open val device: String? = null
    open val operationSystem: String? = null
    open val screenSize: String? = null
    open fun additionParams(jsonObject: JsonObject): JsonObject? = null
}