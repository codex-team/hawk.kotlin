package so.hawk.catcher.provider

/**
 * This is interface user to provide information of user. All caught events are added by user ID
 */
interface UserProvider {
    fun currentUser(): User?

    companion object {
        internal val default = object : UserProvider {
            override fun currentUser(): User? = null
        }
    }
}

/**
 * It is an information model of the user who has events
 * @param id unique user ID
 * @param name name of user, if present. By default, is empty string
 */
data class User(
    val id: String,
    val name: String = ""
)