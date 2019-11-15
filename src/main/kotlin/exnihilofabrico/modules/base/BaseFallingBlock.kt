package exnihilofabrico.modules.base

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.FallingBlock

open class BaseFallingBlock(settings: FabricBlockSettings): FallingBlock(settings.build())