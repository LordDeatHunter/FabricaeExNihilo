package exnihilofabrico.util

import exnihilofabrico.id
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

enum class EnumVanillaWoodTypes(val text: String) {
    OAK("oak"),
    SPRUCE("spruce"),
    BIRCH("birch"),
    JUNGLE("jungle"),
    ACACIA("acacia"),
    DARK_OAK("dark_oak");

    fun getTexturePlanks() = Identifier("minecraft", "block/${text}_planks")
    fun getTextureLog() = Identifier("minecraft", "block/${text}_log")
    fun getTextureLogTop() = Identifier("minecraft", "block/${text}_log_top")

    fun getLeafBlock() = Registry.BLOCK[getLeafID()]
    fun getLogBlock() = Registry.BLOCK[getLogID()]
    fun getPlanksBlock() = Registry.BLOCK[getPlanksID()]
    fun getSlabBlock() = Registry.BLOCK[getSlabID()]
    fun getSaplingBlock() = Registry.BLOCK[getSaplingID()]
    fun getSeedItem() = Registry.ITEM[getSeedID()]


    fun getLeafID() = Identifier("minecraft", "${text}_leaves")
    fun getLogID() = Identifier("minecraft", "${text}_log")
    fun getPlanksID() = Identifier("minecraft", "${text}_planks")
    fun getSlabID() = Identifier("minecraft", "${text}_slab")
    fun getSaplingID() = Identifier("minecraft", "${text}_sapling")
    fun getSeedID() = id("seed_$text")

}