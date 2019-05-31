package exnihilofabrico.content.fluids

import exnihilofabrico.util.PlayerHelper
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.block.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object WitchWater: ExNihiloFluid() {
    override val block: FluidBlock = object: BaseFluidBlock(this, Block.Settings.of(Material.WATER).noCollision()) {
        override fun onEntityCollision(state: BlockState?, world: World?, pos: BlockPos?, entity: Entity?) {
            when(entity){
                // TODO WitchWater Entity Transforms
                is PlayerEntity -> {
                    PlayerHelper.applyPotion(entity, StatusEffectInstance(StatusEffects.BLINDNESS, 210, 0))
                    PlayerHelper.applyPotion(entity, StatusEffectInstance(StatusEffects.WEAKNESS, 210, 2))
                    PlayerHelper.applyPotion(entity, StatusEffectInstance(StatusEffects.WITHER, 210, 0))
                    PlayerHelper.applyPotion(entity, StatusEffectInstance(StatusEffects.SLOWNESS, 210, 0))
                }
            }
        }

        override fun receiveNeighborFluids(world: World, pos: BlockPos, state: BlockState): Boolean {
            // TODO WitchWaterWorld
            return true
        }
    }
}