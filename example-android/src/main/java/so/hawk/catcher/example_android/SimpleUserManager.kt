package so.hawk.catcher.example_android

import java.util.UUID

class SimpleUserManager {
    private lateinit var _name: String
    private lateinit var _id: String
    var name: String
        set(value) {
            _name = value
            _id = UUID.randomUUID().toString()
        }
        get() {
            return _name
        }

    val id: String
        get() = _id

    fun isInitialize(): Boolean = ::_name.isInitialized
}