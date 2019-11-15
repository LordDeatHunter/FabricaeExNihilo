package exnihilofabrico.util

import net.fabricmc.fabric.api.block.FabricBlockSettings

// Vanilla Block$Settings style single member strength function
fun FabricBlockSettings.strength(f: Float): FabricBlockSettings = this.strength(f, f)

