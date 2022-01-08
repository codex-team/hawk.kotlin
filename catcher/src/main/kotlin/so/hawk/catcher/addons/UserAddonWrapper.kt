package so.hawk.catcher.addons

import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * Wrapper for converting from [CustomAddon] to [Addon]
 */
internal class CustomAddonWrapper(
    private val customAddon: CustomAddon,
    private val gson: Gson
) : Addon {
    /**
     * Fill json object of information from map
     * @param jsonObject Json object that put all information
     */
    override fun fillJsonObject(jsonObject: JsonObject) {
        jsonObject.add(customAddon.name, gson.toJsonTree(customAddon.provideData()))
    }
}