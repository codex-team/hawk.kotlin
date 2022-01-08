package so.hawk.catcher

import so.hawk.catcher.provider.VersionProvider

/**
 * Work with version provider
 * @param token Hawk token
 * @param versionProvider Contains version information of application
 */
internal class DefaultSettingProvider(
    token: String,
    private val versionProvider: VersionProvider
) : HawkSettingProvider {
    /**
     * Hawk token
     */
    private val _token = token

    /**
     * Get version name of application
     * @return version name
     */
    override val versionName: String
        get() = versionProvider.getVersionName()

    /**
     * Get application version code
     * @return version of application
     */
    override val appVersion: Int
        get() = versionProvider.getAppVersion()

    /**
     * Get Hawk token
     * @return Hawk token
     */
    override val token: String
        get() = _token
}