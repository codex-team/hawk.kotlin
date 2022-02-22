package so.hawk.catcher

import so.hawk.catcher.provider.DeviceSpecificProvider
import so.hawk.catcher.provider.UserProvider
import so.hawk.catcher.provider.VersionProvider

interface IHawkCatcher {

    fun versionProvider(versionProvider: VersionProvider): IHawkCatcher
    fun userProvider(userProvider: UserProvider): IHawkCatcher
    fun deviceSpecificProvider(deviceSpecificProvider: DeviceSpecificProvider): IHawkCatcher
    fun isDebug(isDebug: Boolean): IHawkCatcher
    fun build(): HawkExceptionCatcher
}