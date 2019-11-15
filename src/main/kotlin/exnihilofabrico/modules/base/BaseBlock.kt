package exnihilofabrico.modules.base

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block

open class BaseBlock(settings: FabricBlockSettings): Block(settings.build())