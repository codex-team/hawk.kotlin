package so.hawk.catcher.addons

/**
 * Interface for additional information
 */
interface CustomAddon {
    val name: String
    fun provideData(): Map<String, Any>
}