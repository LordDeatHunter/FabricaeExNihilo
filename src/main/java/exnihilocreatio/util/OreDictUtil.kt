package exnihilocreatio.util

import exnihilocreatio.config.ModConfig
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.oredict.OreDictionary

object OreDictUtil {
    @JvmStatic
    fun getOreDictEntry(oreName: String): ItemInfo? {
        if(OreDictionary.getOres(oreName).isEmpty())
            return null
        for(modid in ModConfig.misc.oreDictPreferenceOrder){
            if(!Loader.isModLoaded(modid))
                continue
            val possibles = OreDictionary.getOres(oreName).filter { it.item.registryName?.namespace?.equals(modid) ?: false }
            if(possibles.isNotEmpty())
                return ItemInfo(possibles[0])
        }
        return ItemInfo(OreDictionary.getOres(oreName)[0])
    }
}