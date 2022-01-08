package so.hawk.catcher.configurations

import so.hawk.catcher.addons.Addon
import so.hawk.catcher.addons.CustomAddon

/**
 * Provide information of configuration
 */
internal interface IConfigurations {
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
    val customAddons: List<Addon>

    /**
     * Add user addon
     *
     * @param customAddon
     */
    fun addCustomAddon(customAddon: CustomAddon)

    /**
     * Remove user addon
     *
     * @param customAddon
     */
    fun removeCustomAddon(customAddon: CustomAddon)

    /**
     * Remove user addon by [name]
     *
     * @param name Name of user addon
     */
    fun removeCustomAddon(name: String)
}