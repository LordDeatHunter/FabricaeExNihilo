package exnihilocreatio.recipes.defaults

import exnihilocreatio.texturing.Color
import exnihilocreatio.util.ItemInfo
import net.minecraftforge.oredict.OreDictionary

enum class EnumModdedMetals(val color: Color) {
    COPPER(Color("FF9933")),
    TIN(Color("E6FFF2")),
    ALUMINUM(Color("BFBFBF")),
    LEAD(Color("330066")),
    SILVER(Color("F2F2F2")),
    NICKEL(Color("FFFFCC")),
    URANIUM(Color("4E5B43")),
    ZINC(Color("A59C74")),
    TUNGSTEN(Color("1C1C1C"));

    fun getRegistryName(): String {
        return name.toLowerCase()
    }

    private fun getConcatableName(): String {
        return getRegistryName().capitalize()
    }

    fun getOreDictName(): String {
        return "ore" + getConcatableName()
    }

    /**
     * Attempt to grab the first ingot from the ore dictionary to be the registered ingot
     */
    fun getIngot(): ItemInfo? {
        if(OreDictionary.getOres("ingot"+getConcatableName()).isEmpty())
            return null
        return ItemInfo(OreDictionary.getOres("ingot"+getConcatableName())[0])
    }
}