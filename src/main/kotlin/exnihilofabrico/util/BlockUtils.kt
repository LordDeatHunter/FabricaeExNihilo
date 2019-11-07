package exnihilofabrico.util

import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.state.AbstractPropertyContainer

// Vanilla Block$Settings style single member strength function
fun FabricBlockSettings.strength(f: Float) = this.strength(f, f)
