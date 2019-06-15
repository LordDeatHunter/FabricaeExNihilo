package exnihilofabrico.common.tools

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.IToolRegistry
import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterial
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class ToolItemWithRegistry(material: ToolMaterial, val registry: IToolRegistry, settings: Item.Settings):
    ToolItem(material, settings.itemGroup(ExNihiloFabrico.ITEM_GROUP)) {

    private val blockBreakingSpeed: Float = material.blockBreakingSpeed

    override fun isEffectiveOn(state: BlockState) = registry.isRegistered(state.block)

    override fun getBlockBreakingSpeed(stack: ItemStack, state: BlockState) =
        if(isEffectiveOn(state)) blockBreakingSpeed else 1f

    override fun onBlockBroken(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, entity: LivingEntity?): Boolean {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f)
            stack.applyDamage(1, entity, { ent -> ent?.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND) })
        return true
    }
}