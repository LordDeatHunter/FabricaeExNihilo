package exnihilofabrico.common

import exnihilofabrico.ExNihiloConfig
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.id
import exnihilofabrico.content.farming.PlantableItem
import exnihilofabrico.content.farming.TransformingItem
import exnihilofabrico.content.sieves.MeshItem
import exnihilofabrico.util.Color
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.KelpBlock
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.registry.Registry

object ModItems {
    val seed_settings = Item.Settings().itemGroup(ExNihiloFabrico.ITEM_GROUP).stackSize(64)
    val mesh_settings = Item.Settings().itemGroup(ExNihiloFabrico.ITEM_GROUP).stackSize(ExNihiloConfig.Modules.Sieve.meshStackSize)

    // Tree Seeds
    val SEED_OAK = PlantableItem(Blocks.OAK_SAPLING, seed_settings)
    val SEED_BIRCH = PlantableItem(Blocks.BIRCH_SAPLING, seed_settings)
    val SEED_SPRUCE = PlantableItem(Blocks.SPRUCE_SAPLING, seed_settings)
    val SEED_JUNGLE = PlantableItem(Blocks.JUNGLE_SAPLING, seed_settings)
    val SEED_ACACIA = PlantableItem(Blocks.ACACIA_SAPLING, seed_settings)
    val SEED_DARK_OAK = PlantableItem(Blocks.DARK_OAK_SAPLING, seed_settings)

    // Crop Seeds
    val SEED_CARROT = PlantableItem(Blocks.CARROTS, seed_settings)
    val SEED_POTATO = PlantableItem(Blocks.POTATOES, seed_settings)
    val SEED_SUGARCANE = PlantableItem(Blocks.SUGAR_CANE, seed_settings)
    val SEED_CACTUS = PlantableItem(Blocks.CACTUS, seed_settings)
    val SEED_CHORUS = PlantableItem(Blocks.CHORUS_FLOWER, seed_settings)
    val SEED_KELP: PlantableItem // Fancy declaration in init block

    // Flower Seeds
    // TODO double tall seeds
    //val SEED_SUNFLOWER = PlantableItem(Blocks.SUNFLOWER, seed_settings)
    //val SEED_LILAC = PlantableItem(Blocks.LILAC, seed_settings)
    //val SEED_ROSE_BUSH = PlantableItem(Blocks.ROSE_BUSH, seed_settings)
    //val SEED_PEONY = PlantableItem(Blocks.PEONY, seed_settings)

    // Misc Seeds
    val SEED_SEA_PICKLE = PlantableItem(Blocks.SEA_PICKLE, seed_settings)

    // Transforming seeds
    val SEED_GRASS = TransformingItem(Blocks.DIRT, Blocks.GRASS_BLOCK, seed_settings)
    val SEED_MYCELIUM = TransformingItem(Blocks.DIRT, Blocks.MYCELIUM, seed_settings)


    // Meshes
    val MESH_STRING = MeshItem(Color.WHITE, mesh_settings)
    val MESH_FLINT = MeshItem(Color.DARK_GRAY, mesh_settings)
    val MESH_IRON = MeshItem(Color.GRAY, mesh_settings)
    val MESH_DIAMOND = MeshItem(Color.AQUA, mesh_settings)

    init {
        val kelpStates = ArrayList<BlockState>()
        for(i in 0..25) {
            kelpStates.add(Blocks.KELP.defaultState.with(KelpBlock.AGE, i))
        }
        SEED_KELP = object: PlantableItem(kelpStates, seed_settings) {
            override fun placementCheck(context: ItemUsageContext): Boolean {
                return context.world.getFluidState(context.blockPos.offset(context.facing)).fluid == Fluids.WATER
            }
        }
    }

    fun registerItems(registry: Registry<Item>) {
        Registry.register(registry, id("seed_oak"), SEED_OAK)
        Registry.register(registry, id("seed_birch"), SEED_BIRCH)
        Registry.register(registry, id("seed_spruce"), SEED_SPRUCE)
        Registry.register(registry, id("seed_jungle"), SEED_JUNGLE)
        Registry.register(registry, id("seed_acacia"), SEED_ACACIA)
        Registry.register(registry, id("seed_dark_oak"), SEED_DARK_OAK)
        Registry.register(registry, id("seed_carrot"), SEED_CARROT)
        Registry.register(registry, id("seed_potato"), SEED_POTATO)
        Registry.register(registry, id("seed_sugarcane"), SEED_SUGARCANE)
        Registry.register(registry, id("seed_cactus"), SEED_CACTUS)
        Registry.register(registry, id("seed_chorus"), SEED_CHORUS)
        Registry.register(registry, id("seed_kelp"), SEED_KELP)
        //Registry.register(registry, id("seed_sunflower"), SEED_SUNFLOWER)
        //Registry.register(registry, id("seed_lilac"), SEED_LILAC)
        //Registry.register(registry, id("seed_rose_bush"), SEED_ROSE_BUSH)
        //Registry.register(registry, id("seed_peony"), SEED_PEONY)
        Registry.register(registry, id("seed_sea_pickle"), SEED_SEA_PICKLE)
        Registry.register(registry, id("seed_grass"), SEED_GRASS)
        Registry.register(registry, id("seed_mycelium"), SEED_MYCELIUM)

        Registry.register(registry, id("mesh_string"), MESH_STRING)
        Registry.register(registry, id("mesh_flint"), MESH_FLINT)
        Registry.register(registry, id("mesh_iron"), MESH_IRON)
        Registry.register(registry, id("mesh_diamond"), MESH_DIAMOND)
    }
}