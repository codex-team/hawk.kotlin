package so.hawk.catcher

interface VersionProvider {
    companion object {
        val defaultVersionProvider = object : VersionProvider {
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