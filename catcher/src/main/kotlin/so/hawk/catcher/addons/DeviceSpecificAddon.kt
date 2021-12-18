package so.hawk.catcher.addons

import com.google.gson.JsonObject
import so.hawk.catcher.DeviceInfo

/**
 * Addon that apply Device Specific information to event
 * @param deviceInfo class that provide information about device
 */
class DeviceSpecificAddon(private val deviceInfo: DeviceInfo) : Addon {
    override fun fillJsonObject(jsonObject: JsonObject) {
        jsonObject.addProperty("screenSize", deviceInfo.getScreenSize())
    }
}