package exnihilofabrico.util

import net.minecraft.util.Identifier

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

}