package so.hawk.catcher

interface HawkSettingProvider {
    /**
     * Get version name of application
     * @return version name
     */
    fun getVersionName(): String

    /**
     * Get application version code
     * @return version of application
     */
    fun getAppVersion(): Int

    /**
     * Get Hawk token
     * @return Hawk token
     */
    fun getToken(): String
}