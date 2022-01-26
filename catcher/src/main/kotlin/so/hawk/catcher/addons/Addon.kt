package so.hawk.catcher.addons

import com.google.gson.JsonObject

/**
 * Interface that can apply own information to event
 */
interface Addon {
    /**
     * Apply data to json event
     * @param jsonObject object that need put inside data
     */
    fun fillJsonObject(jsonObject: JsonObject)
}