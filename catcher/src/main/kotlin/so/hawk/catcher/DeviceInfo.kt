package so.hawk.catcher

import java.awt.Toolkit

/**
 * Class that know about device information
 * @param context required to get [WindowManager]
 */
class DesktopDeviceInfo() {

    /**
     * Provide screen size in format WIDTHxHEIGHT
     * @return screen size like as string
     * @sample 360x640
     */
    fun getScreenSize(): String {
        return kotlin.runCatching {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            val width = screenSize.width
            val height = screenSize.height
            "${width}x$height"
        }.getOrDefault("")
    }
}

interface DeviceInfo {
    fun getScreenSize(): String
}