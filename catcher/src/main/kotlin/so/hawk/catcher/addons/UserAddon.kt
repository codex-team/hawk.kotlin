package so.hawk.catcher.addons

interface UserAddon {
    val name: String
    fun provideData(): Map<String, Any>
}