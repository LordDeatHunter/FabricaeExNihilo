package exnihilocreatio.api.registries

interface IBarrelLiquidBlacklistRegistry : IRegistryMappedList<Int, String> {
    fun isBlacklisted(level: Int, fluid: String): Boolean
    fun register(level: Int, fluid: String)
}