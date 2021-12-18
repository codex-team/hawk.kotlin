package so.hawk.catcher

class HawkCatcher(
    private val token: String
) {
    private var _versionProvider = VersionProvider.defaultVersionProvider
    private var _userProvider: UserProvider = object : UserProvider {
        override fun currentUser(): User? = null
    }

    fun addVersionProvider(versionProvider: VersionProvider): HawkCatcher {
        _versionProvider = versionProvider
        return this
    }

    fun addUserProvider(userProvider: UserProvider): HawkCatcher {
        _userProvider = userProvider
        return this
    }

    fun build(): HawkExceptionCatcher {
        return HawkExceptionCatcher(token, _versionProvider, _userProvider)
    }

}