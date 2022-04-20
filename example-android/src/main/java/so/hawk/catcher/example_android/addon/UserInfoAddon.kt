package so.hawk.catcher.example_android.addon

import so.hawk.catcher.addons.CustomAddon
import so.hawk.catcher.example_android.SimpleUserManager


class UserInfoAddon(private val userManager: SimpleUserManager) : CustomAddon {
    override val name: String = "user"
    override fun provideData(): Map<String, Any> {
        return if (userManager.isInitialize()) {
            mapOf("name" to userManager.name)
        } else {
            emptyMap()
        }
    }
}