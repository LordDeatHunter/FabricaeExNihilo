package exnihilofabrico.common.tools

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.common.ModTags.CROOK_TAG
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.*

class CrookTool(material: ToolMaterial, settings: Item.Settings):
    ToolItem(material, settings.itemGroup(ExNihiloFabrico.ITEM_GROUP)) {

    private val blockBreakingSpeed: Float = material.blockBreakingSpeed

    override fun isEffectiveOn(state: BlockState): Boolean {
        return ExNihiloRegistries.CROOK.isRegistered(state.block)
    }

    override fun getBlockBreakingSpeed(stack: ItemStack?, state: BlockState?) =
        if (isEffectiveOn(state ?: Blocks.AIR.defaultState)) blockBreakingSpeed else 1f

    companion object {
        @JvmStatic
        fun isCrook(stack: ItemStack): Boolean {
            if(stack.item is CrookTool || stack.item.matches(CROOK_TAG))
                return true
            return false
        }
    }

}