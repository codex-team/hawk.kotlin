package so.hawk.catcher

/**
 * Work with <meta-data> and package information
 */
internal class DefaultSettingProvider(
    token: String,
    private val versionProvider: VersionProvider
) : HawkSettingProvider {
    companion object {
        /**
         * Key for getting token from <meta-data>
         */
        private const val HAWK_CATCHER_TOKEN_KEY = "hawk_catcher_token"

        /**
         * Default value if token not found in <meta-data>
         */
        const val UNKNOWN_TOKEN = ""
    }

    /**
     * Hawk token from <meta-data>
     */
    private val _token = token

    /**
     * Get version name of application
     * @return version name
     */
    override fun getVersionName(): String = versionProvider.getVersionName()

    /**
     * Get application version code
     * @return version of application
     */
    override fun getAppVersion(): Int = versionProvider.getAppVersion()

    /**
     * Get Hawk token
     * @return Hawk token
     */
    override fun getToken(): String = _token
}