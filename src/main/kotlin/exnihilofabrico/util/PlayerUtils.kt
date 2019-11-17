package exnihilofabrico.util

import alexiil.mc.lib.attributes.Simulation
import net.minecraft.entity.player.PlayerEntity

fun PlayerEntity.isSimulating() = if(this.isCreative) Simulation.SIMULATE else Simulation.ACTION