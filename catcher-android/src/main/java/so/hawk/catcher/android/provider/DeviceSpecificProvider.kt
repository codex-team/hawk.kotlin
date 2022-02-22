package so.hawk.catcher.android.provider

import android.os.Build
import com.google.gson.JsonObject
import so.hawk.catcher.android.DeviceInfo
import so.hawk.catcher.provider.DeviceSpecificProvider

internal class DeviceSpecificProvider(private val deviceInfo: DeviceInfo) : DeviceSpecificProvider() {
    override val device: String
        get() = Build.DEVICE
    override val operationSystem: String
        get() = Build.MANUFACTURER
    override val screenSize: String
        get() = deviceInfo.getScreenSize()

    override fun additionParams(jsonObject: JsonObject): JsonObject? {
        jsonObject.addProperty("brand", Build.BRAND)
        jsonObject.addProperty("model", Build.MODEL)
        jsonObject.addProperty("product", Build.PRODUCT)
        jsonObject.addProperty("SDK", Build.VERSION.SDK_INT)
        jsonObject.addProperty("release", Build.VERSION.RELEASE)
        return super.additionParams(jsonObject)
    }
}