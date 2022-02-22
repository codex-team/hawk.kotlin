package so.hawk.catcher

import android.content.Context
import so.hawk.catcher.android.DeviceInfo
import so.hawk.catcher.android.MetaDataProvider
import so.hawk.catcher.android.provider.AndroidVersionProvider
import so.hawk.catcher.provider.DeviceSpecificProvider
import so.hawk.catcher.provider.UserProvider
import so.hawk.catcher.provider.VersionProvider

internal typealias AndroidDeviceSpecificProvider = so.hawk.catcher.android.provider.DeviceSpecificProvider

class AndroidHawkCatcher(context: Context) : IHawkCatcher {
    private val metaDataProvider = MetaDataProvider(context)
    private val hawkCatcher = HawkCatcher(metaDataProvider.getToken())

    init {
        hawkCatcher.versionProvider(AndroidVersionProvider(metaDataProvider))
        hawkCatcher.deviceSpecificProvider(AndroidDeviceSpecificProvider(DeviceInfo(context)))
    }

    override fun versionProvider(versionProvider: VersionProvider): AndroidHawkCatcher {
        hawkCatcher.versionProvider(versionProvider)
        return this
    }

    override fun deviceSpecificProvider(deviceSpecificProvider: DeviceSpecificProvider): IHawkCatcher {
        hawkCatcher.deviceSpecificProvider(deviceSpecificProvider)
        return this
    }

    override fun userProvider(userProvider: UserProvider): AndroidHawkCatcher {
        hawkCatcher.userProvider(userProvider)
        return this
    }

    override fun isDebug(isDebug: Boolean): AndroidHawkCatcher {
        hawkCatcher.isDebug(isDebug)
        return this
    }

    override fun build(): HawkExceptionCatcher {
        return hawkCatcher.build()
    }

}