package so.hawk.catcher

import com.google.gson.JsonParser
import so.hawk.catcher.TokenParser.Companion.INTEGRATION_ID_KEY
import so.hawk.catcher.TokenParser.Companion.UNKNOWN_INTEGRATION_ID
import java.nio.charset.Charset
import java.util.Base64

/**
 * Implementation of [TokenParser] with using gson library for parsing json
 */
class TokenParserImpl : TokenParser {

    override fun parse(encodedToken: String): String {
        return runCatching {
            Base64
                .getDecoder()
                .decode(encodedToken)
                .toString(Charset.defaultCharset())
                .let(JsonParser::parseString)
                .asJsonObject
                .getAsJsonPrimitive(INTEGRATION_ID_KEY)
                .asString
        }.getOrNull() ?: UNKNOWN_INTEGRATION_ID

    }
}

/**
 * Interface for parsing encoded token and get integration id
 */
interface TokenParser {
    companion object {
        /**
         * Key for getting integration id from token
         */
        const val INTEGRATION_ID_KEY = "integrationId"

        /**
         * Default value for unknown token
         */
        const val UNKNOWN_INTEGRATION_ID = "unknown"
    }

    /**
     * Get encoded token and return integration id
     * @param encodedToken Encoded token
     * @return integration id
     */
    fun parse(encodedToken: String): String
}