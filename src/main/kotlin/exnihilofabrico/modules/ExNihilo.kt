package exnihilofabrico.modules

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.registry.*
import exnihilofabrico.common.ModItems
import net.minecraft.block.Blocks
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

object ExNihilo: ICompatModule {
    override fun registerBarrelAlchemy(registry: IBarrelAlchemyRegistry) {

    }

    override fun registerBarrelMilking(registry: IBarrelMilkingRegistry) {

    }

    override fun registerCrucibleHeat(registry: ICrucibleHeatRegistry) {
        registry.register(Blocks.TORCH, 1)
    }

    override fun registerCrucibleStone(registry: ICrucibleRegistry) {

    }

    override fun registerCrucibleWood(registry: ICrucibleRegistry) {

    }

    override fun registerSieve(registry: ISieveRegistry) {
        registry.register(ModItems.MESH_STRING, Blocks.GRAVEL, Lootable(ItemStack(Items.FLINT, 1), .5))
        registry.register(ModItems.MESH_FLINT, Blocks.GRAVEL, Lootable(ItemStack(Items.FLINT, 1), .3))

        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.SAND, Lootable(ModItems.SEED_SUGARCANE.defaultStack, listOf(.5)))
        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.SAND, Lootable(ModItems.SEED_KELP.defaultStack, listOf(.3, .2)))
        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH.defaultStack, listOf(.1, .2, .3)))
        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH.defaultStack, listOf(.1, .2, .3)))

        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(ModItems.SEED_SUGARCANE.defaultStack, listOf(.3)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(ModItems.SEED_KELP.defaultStack, listOf(.5, .5)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(ModItems.SEED_SEA_PICKLE.defaultStack, listOf(.1)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH.defaultStack, listOf(.4)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH.defaultStack, listOf(.5)))
    }

    override fun registerCrook(registry: IToolRegistry) {

    }

    override fun registerHammer(registry: IToolRegistry) {

    }

    override fun registerWitchWaterEntity(registry: IWitchWaterEntityRegistry) {

    }

    override fun registerWitchWaterFluid(registry: IWitchWaterFluidRegistry) {

    }

}