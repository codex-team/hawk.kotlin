package so.hawk.catcher.android.provider

import so.hawk.catcher.android.MetaDataProvider
import so.hawk.catcher.provider.VersionProvider

internal class AndroidVersionProvider(private val metaDataProvider: MetaDataProvider) : VersionProvider {
    override fun getVersionName(): String = metaDataProvider.getVersionName()

    override fun getAppVersion(): Int = metaDataProvider.getAppVersion()
}