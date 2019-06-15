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

    fun getTexturePlanks() = Identifier("minecraft", "textures/block/${text}_planks")
    fun getTextureLog() = Identifier("minecraft", "textures/block/${text}_log")
    fun getTextureLogTop() = Identifier("minecraft", "textures/block/${text}_log_top")

    fun getLeafBlock() = Registry.BLOCK[Identifier("minecraft", "${text}_leaves")]
    fun getLogBlock() = Registry.BLOCK[Identifier("minecraft", "${text}_log")]
    fun getPlanksBlock() = Registry.BLOCK[Identifier("minecraft", "${text}_planks")]
    fun getSaplingBlock() = Registry.BLOCK[Identifier("minecraft", "${text}_sapling")]
    fun getSeedItem() = Registry.ITEM[id("seed_$text")]

}