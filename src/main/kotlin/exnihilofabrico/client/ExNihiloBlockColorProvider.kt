package exnihilofabrico.client

import exnihilofabrico.modules.base.IHasColor
import exnihilofabrico.util.Color
import net.minecraft.block.BlockState
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

object ExNihiloBlockColorProvider: BlockColorProvider {
    override fun getColor(state: BlockState?, view: BlockRenderView?, pos: BlockPos?, index: Int) =
        (state?.block as? IHasColor)?.getColor(index) ?: Color.WHITE.toInt()

}