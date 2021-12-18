package so.hawk.catcher.addons

import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * Wrapper for converting from [UserAddon] to [Addon]
 */
class UserAddonWrapper(
    private val userAddon: UserAddon,
    private val gson: Gson
) : Addon {
    /**
     * Fill json object of information from map
     * @param jsonObject Json object that put all information
     */
    override fun fillJsonObject(jsonObject: JsonObject) {
        jsonObject.add(userAddon.name, gson.toJsonTree(userAddon.provideData()))
    }
}