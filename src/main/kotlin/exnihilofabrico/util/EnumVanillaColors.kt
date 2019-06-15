package exnihilofabrico.util

import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

enum class EnumVanillaColors {
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    fun getConcrete() = Registry.BLOCK[Identifier("${name.toLowerCase()}_concrete")]
    fun getConcretePowder() = Registry.BLOCK[Identifier("${name.toLowerCase()}_concrete_powder")]
    fun getWool() = Registry.BLOCK[Identifier("${name.toLowerCase()}_wool")]
    fun getDye() = Registry.ITEM[Identifier("${name.toLowerCase()}_dye")]
    fun getGlass() = Registry.BLOCK[Identifier("${name.toLowerCase()}_stained_glass")]
    fun getGlassPane() = Registry.BLOCK[Identifier("${name.toLowerCase()}_stained_glass_pane")]
    fun getTerracotta() = Registry.BLOCK[Identifier("${name.toLowerCase()}_terracotta")]
    fun getGlazedTerracotta() = Registry.BLOCK[Identifier("${name.toLowerCase()}_glazed_terracotta")]
    fun getCarpet() = Registry.BLOCK[Identifier("${name.toLowerCase()}_carpet")]
}