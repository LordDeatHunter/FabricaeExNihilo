package exnihilofabrico.modules

import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.modules.base.AbstractFluid
import exnihilofabrico.modules.fluids.BloodFluid
import exnihilofabrico.modules.fluids.BrineFluid
import exnihilofabrico.modules.fluids.MilkFluid
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry

object ModFluids {
    val bucketItemSettings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(1).recipeRemainder(Items.BUCKET).maxCount(1)
    val blockSettings: Block.Settings = FabricBlockSettings.of(Material.WATER).noCollision().strength(100.0f, 100.0f).dropsNothing().build()


    val FLUIDS = listOf<AbstractFluid>(
        WitchWaterFluid.still,
        MilkFluid.still,
        BrineFluid.still,
        BloodFluid.still
    )

    fun registerFluids(registry: Registry<Fluid>) {
        FLUIDS.forEach { it.registerFluids(registry) }
    }

    fun registerFluidBlocks(registry: Registry<Block>) {
        FLUIDS.forEach { it.registerFluidBlock(registry) }
    }

    fun registerBuckets(registry: Registry<Item>) {
        FLUIDS.filter { it != MilkFluid.still }
            .forEach { it.registerBucket(registry) }
    }
}