package exnihilofabrico.modules.infested

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.modules.base.IHasColor
import exnihilofabrico.util.Color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.LeavesBlock
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class InfestedLeavesBlock(val leafBlock: LeavesBlock, settings: FabricBlockSettings): LeavesBlock(settings.build()), IHasColor {
    override fun getColor(index: Int) = Color.WHITE.toInt()


    override fun hasRandomTicks(blockState_1: BlockState?) = true
    override fun onRandomTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        super.onRandomTick(state, world, pos, random)
        if(world.isClient) return
        InfestedHelper.tryToSpreadFrom(
            world,
            pos,
            ExNihiloFabrico.config.modules.silkworms.infestedSpreadAttempts
        )
    }

    @Environment(EnvType.CLIENT)
    override fun getName(): Text {
        return TranslatableText("block.exnihilofabrico.infested", leafBlock.name)
    }
}