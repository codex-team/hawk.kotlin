package so.hawk.catcher.example_android

import android.app.Application
import so.hawk.catcher.AndroidHawkCatcher
import so.hawk.catcher.HawkExceptionCatcher
import so.hawk.catcher.example_android.provider.UserProvider

class HawkApplication : Application() {
    companion object {
        lateinit var hawkExceptionCatcher: HawkExceptionCatcher
        lateinit var userManager: SimpleUserManager
    }


    override fun onCreate() {
        super.onCreate()
        userManager = SimpleUserManager()
        val hawkCatcher = AndroidHawkCatcher(this)
        hawkCatcher.userProvider(UserProvider(userManager))

        hawkExceptionCatcher = hawkCatcher.build()
        // or instead of using UserProvider you can use
        // hawkExceptionCatcher.addCustomAddon(UserInfoAddon(userManager))
        hawkExceptionCatcher.start()
    }
}