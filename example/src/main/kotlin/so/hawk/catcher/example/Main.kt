package so.hawk.catcher.example

import so.hawk.catcher.HawkCatcher
import so.hawk.catcher.provider.User
import so.hawk.catcher.provider.UserProvider

class Main {
    companion object {
        private const val integrationToken = "YOUR_INTEGRATION_TOKEN"

        @JvmStatic
        fun main(args: Array<String>) {
            val catcher =
                HawkCatcher(integrationToken)
                    .userProvider(UserProviderImpl())
                    .isDebug(true)
                    .build()

            catcher.start()

            Main().run()
        }
    }

    fun run() {
        val number = 1 / 0
    }

    class UserProviderImpl() : UserProvider {
        override fun currentUser(): User {
            return User(
                "eyJpbnRlZ3JhdGlvbklkIjoiZWViMzg0OTgtYmNlNC00",
                "Shiplayer"
            )
        }

    }
}