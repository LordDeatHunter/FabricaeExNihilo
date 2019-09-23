package exnihilofabrico.modules.tools

import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.modules.ModTags.CROOK_TAG
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial

class CrookTool(material: ToolMaterial, settings: Item.Settings):
    ToolItemWithRegistry(material, ExNihiloRegistries.CROOK, settings) {
    companion object {
        @JvmStatic
        fun isCrook(stack: ItemStack): Boolean {
            if(stack.item is CrookTool || CROOK_TAG.contains(stack.item))
                return true
            return false
        }
    }

}