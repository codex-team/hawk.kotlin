package so.hawk.catcher.example

import so.hawk.catcher.HawkCatcher
import so.hawk.catcher.User
import so.hawk.catcher.UserProvider
import so.hawk.catcher.VersionProvider

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val catcher =
                HawkCatcher("eyJpbnRlZ3JhdGlvbklkIjoiZWViMzg0OTgtYmNlNC00NzdkLTllNzEtNjQ2MzQ5MjRmMWQ5Iiwic2VjcmV0IjoiZDkxNjVlMjAtZmNhMy00YjcxLThlYmYtMWY0ZTNiMDdkZTE3In0=")
                    .addVersionProvider(VersionProvider.defaultVersionProvider)
                    .addUserProvider(UserProviderImpl())
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
                "Anton"
            )
        }

    }
}