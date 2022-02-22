package so.hawk.catcher.addons

import com.google.gson.JsonObject
import so.hawk.catcher.provider.DeviceSpecificProvider

/**
 * Addon that apply Device Specific information to event
 * @param deviceInfo class that provide information about device
 */
class DeviceSpecificAddon(private val deviceSpecificProvider: DeviceSpecificProvider) : Addon {
    override fun fillJsonObject(jsonObject: JsonObject) {
        jsonObject.addProperty("device", deviceSpecificProvider.device)
        jsonObject.addProperty("operationSystem", deviceSpecificProvider.operationSystem)
        jsonObject.addProperty("screenSize", deviceSpecificProvider.screenSize)
        deviceSpecificProvider.additionParams(jsonObject)
    }
}