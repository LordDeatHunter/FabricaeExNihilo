package exnihilofabrico.util

import exnihilofabrico.api.IWoodDefinition
import exnihilofabrico.id
import net.minecraft.util.Identifier

enum class VanillaWoodDefinitions(val text: String): IWoodDefinition {
    OAK("oak"),
    SPRUCE("spruce"),
    BIRCH("birch"),
    JUNGLE("jungle"),
    ACACIA("acacia"),
    DARK_OAK("dark_oak");

    override fun getTexturePlanks() = Identifier("block/${text}_planks")
    override fun getTextureLog() = Identifier("block/${text}_log")
    override fun getTextureLogTop() = Identifier("block/${text}_log_top")

    override fun getLeafID() = Identifier("${text}_leaves")
    override fun getLogID() = Identifier("${text}_log")
    override fun getPlanksID() = Identifier("${text}_planks")
    override fun getSlabID() = Identifier("${text}_slab")
    override fun getSaplingID() = Identifier("${text}_sapling")

    override fun getSeedID() = id("seed_$text")
}