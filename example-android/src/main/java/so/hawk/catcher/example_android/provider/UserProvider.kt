package so.hawk.catcher.example_android.provider

import so.hawk.catcher.example_android.SimpleUserManager
import so.hawk.catcher.provider.User
import so.hawk.catcher.provider.UserProvider

class UserProvider(private val userManager: SimpleUserManager) : UserProvider {
    override fun currentUser(): User? {
        return if (userManager.isInitialize()) {
            User(
                id = userManager.id,
                name = userManager.name
            )
        } else {
            null
        }
    }
}