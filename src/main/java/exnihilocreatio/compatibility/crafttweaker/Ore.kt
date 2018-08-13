package exnihilocreatio.compatibility.crafttweaker

import crafttweaker.CraftTweakerAPI
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IItemStack
import exnihilocreatio.registries.manager.ExNihiloRegistryManager
import exnihilocreatio.texturing.Color
import exnihilocreatio.util.ItemInfo
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.Optional
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

/**
 * This one is slightly different, it has to be run in the preinit loader to work.
 */
@ZenClass("mods.exnihilocreatio.Ore")
@ZenRegister
object Ore {

    @ZenMethod
    @JvmStatic
    fun removeAll() = ExNihiloRegistryManager.ORE_REGISTRY.clearRegistry()

    @ZenMethod
    @JvmStatic
    fun addRecipe(name: String,
                  color: String,
                  @Optional ingot: IItemStack? = null,
                  @Optional translation: Map<String, String>? = null,
                  @Optional oredict: String? = null) {

        CraftTweakerAPI.logInfo("Adding Ore for $name with color $color")
        ExNihiloRegistryManager.ORE_REGISTRY.register(name, Color(color), if (ingot == null) null else ItemInfo(ingot.internal as ItemStack), translation, oredict)
    }
}
