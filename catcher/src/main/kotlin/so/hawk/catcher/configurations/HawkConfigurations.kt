package so.hawk.catcher.configurations

import com.google.gson.Gson
import so.hawk.catcher.Logger
import so.hawk.catcher.TokenParser
import so.hawk.catcher.TokenParser.Companion.UNKNOWN_INTEGRATION_ID
import so.hawk.catcher.addons.Addon
import so.hawk.catcher.addons.CustomAddon
import so.hawk.catcher.addons.CustomAddonWrapper

/**
 * Common configuration for stable work with default list of addons that should be used for sending
 * additional information
 * @param token for getting integrationId
 * @param defaultAddons default list of addons
 * @param customAddons list with user custom addons
 */
internal class HawkConfigurations(
    token: String,
    private val defaultAddons: List<Addon>,
    customAddons: List<CustomAddon> = emptyList(),
    tokenParser: TokenParser,
    private val gson: Gson,
    private val logger: Logger
) : IConfigurations {

    /**
     * Contains integrationId
     */
    private val _integrationId: String = tokenParser.parse(token)

    /**
     * Default list of addons
     */
    private val _additionalAddons: List<Addon> = mutableListOf()

    /**
     * Map of user addons
     */
    private val _customAddons: MutableMap<String, Addon> =
        customAddons.associateBy(CustomAddon::name) { addon ->
            CustomAddonWrapper(addon, gson)
        }
            .toMutableMap()

    /**
     * Get integrationId
     */
    override val integrationId: String
        get() = _integrationId

    /**
     * Check if integrationId is valid
     */
    override val isCorrect: Boolean
        get() = _integrationId != UNKNOWN_INTEGRATION_ID

    /**
     * Get all list of addons. Contains default and additional list of addons
     */
    override val addons: List<Addon>
        get() = defaultAddons + _additionalAddons

    /**
     * Get list of user addons. Can contains additional user addons. For add new [CustomAddon] use
     * [HawkConfigurations.addCustomAddon]
     */
    override val customAddons: List<Addon>
        get() = _customAddons.values.toList()

    /**
     * Add [CustomAddon] to map, [CustomAddon.name] used as key
     *
     * @param customAddon
     */
    override fun addCustomAddon(customAddon: CustomAddon) {
        if (_customAddons.containsKey(customAddon.name)) {
            logger.w("User addon with name (${customAddon.name}) already added!")
        }
        _customAddons[customAddon.name] = CustomAddonWrapper(customAddon, gson)
    }

    /**
     * Remove [CustomAddon] from map, [CustomAddon.name] used as key
     *
     * @param customAddon
     */
    override fun removeCustomAddon(customAddon: CustomAddon) {
        val addon = _customAddons.remove(customAddon.name)
        if (addon == null) {
            logger.w("User addon with name (${customAddon.name}) already removed!")
        }
    }

    /**
     * Remove [CustomAddon] from map by name, [CustomAddon.name] used as key
     *
     * @param name
     */
    override fun removeCustomAddon(name: String) {
        val addon = _customAddons.remove(name)
        if (addon == null) {
            logger.w("User addon with name ($name) already removed!")
        }
    }
}