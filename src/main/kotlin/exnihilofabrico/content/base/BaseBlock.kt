package exnihilofabrico.content.base

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block

abstract class BaseBlock(settings: FabricBlockSettings): Block(settings.build()) {

}