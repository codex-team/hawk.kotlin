package so.hawk.catcher

interface UserProvider {
    fun currentUser(): User?
}

data class User(
    val id: String,
    val name: String
)