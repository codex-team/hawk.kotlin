package so.hawk.catcher

internal interface HawkSettingProvider {
    /**
     * Get version name of application
     * @return version name
     */
    val versionName: String

    /**
     * Get application version code
     * @return version of application
     */
    val appVersion: Int

    /**
     * Get Hawk token
     * @return Hawk token
     */
    val token: String
}