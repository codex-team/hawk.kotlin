package so.hawk.catcher.configurations

import com.google.gson.Gson
import so.hawk.catcher.Logger
import so.hawk.catcher.TokenParser
import so.hawk.catcher.TokenParser.Companion.UNKNOWN_INTEGRATION_ID
import so.hawk.catcher.addons.Addon
import so.hawk.catcher.addons.UserAddon
import so.hawk.catcher.addons.UserAddonWrapper

/**
 * Common configuration for stable work with default list of addons that should be apply to sending
 * additional information
 * @param token for getting integrationId
 * @param defaultAddons default list of addons
 * @param userAddons list with user custom addons
 */
class HawkConfigurations(
    val token: String,
    private val defaultAddons: List<Addon>,
    userAddons: List<UserAddon> = emptyList(),
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
    private val _userAddons: MutableMap<String, Addon> =
        userAddons.associateBy(UserAddon::name) { addon ->
            UserAddonWrapper(addon, gson)
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
     * Get list of user addons. Can contains additional user addons. For add new [UserAddon] use
     * [HawkConfigurations.addUserAddon]
     */
    override val userAddons: List<Addon>
        get() = _userAddons.values.toList()

    /**
     * Add [UserAddon] to map, [UserAddon.name] used as key
     *
     * @param userAddon
     */
    override fun addUserAddon(userAddon: UserAddon) {
        if (_userAddons.containsKey(userAddon.name)) {
            logger.w("User addon with name (${userAddon.name}) already added!")
        }
        _userAddons[userAddon.name] = UserAddonWrapper(userAddon, gson)
    }

    /**
     * Remove [UserAddon] from map, [UserAddon.name] used as key
     *
     * @param userAddon
     */
    override fun removeUserAddon(userAddon: UserAddon) {
        val addon = _userAddons.remove(userAddon.name)
        if (addon == null) {
            logger.w("User addon with name (${userAddon.name}) already removed!")
        }
    }

    /**
     * Remove [UserAddon] from map by name, [UserAddon.name] used as key
     *
     * @param name
     */
    override fun removeUserAddon(name: String) {
        val addon = _userAddons.remove(name)
        if (addon == null) {
            logger.w("User addon with name ($name) already removed!")
        }
    }
}