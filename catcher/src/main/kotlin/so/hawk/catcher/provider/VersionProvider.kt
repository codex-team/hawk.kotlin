package so.hawk.catcher.provider

/**
 * This is interface used to implement your own name and app version value.
 */
interface VersionProvider {
    companion object {
        /**
         * Default version provider
         * Default value of name is "v1"
         * Default value of app version is 1
         */
        internal val defaultVersionProvider = object : VersionProvider {
            override fun getVersionName(): String = "v${getAppVersion()}"

            override fun getAppVersion(): Int = 1
        }
    }

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
}