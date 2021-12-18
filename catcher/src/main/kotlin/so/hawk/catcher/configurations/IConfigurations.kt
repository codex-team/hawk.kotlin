package so.hawk.catcher.configurations

import so.hawk.catcher.addons.Addon
import so.hawk.catcher.addons.UserAddon

/**
 * Provide information of configuration
 */
interface IConfigurations {
    /**
     * Integration id that need to configure URL for sending event
     */
    val integrationId: String

    /**
     * Check if [integrationId] is valid
     */
    val isCorrect: Boolean

    /**
     * Provide list of addons that apply to event before sending
     */
    val addons: List<Addon>

    /**
     * Provide list of user addons that apply to event before sending
     */
    val userAddons: List<Addon>

    /**
     * Add user addon
     *
     * @param userAddon
     */
    fun addUserAddon(userAddon: UserAddon)

    /**
     * Remove user addon
     *
     * @param userAddon
     */
    fun removeUserAddon(userAddon: UserAddon)

    /**
     * Remove user addon by [name]
     *
     * @param name Name of user addon
     */
    fun removeUserAddon(name: String)
}