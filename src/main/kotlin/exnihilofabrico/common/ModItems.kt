package exnihilofabrico.common

import exnihilofabrico.ExNihiloConfig
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.common.base.BaseItem
import exnihilofabrico.common.farming.PlantableItem
import exnihilofabrico.common.farming.SilkWormItem
import exnihilofabrico.common.farming.TransformingItem
import exnihilofabrico.common.sieves.MeshItem
import exnihilofabrico.id
import exnihilofabrico.util.Color
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.KelpBlock
import net.minecraft.fluid.Fluids
import net.minecraft.item.FoodComponents
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModItems {
    val seed_settings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(64)
    val mesh_settings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(ExNihiloConfig.Modules.Sieve.meshStackSize)

    val TREE_SEEDS = mutableMapOf<Identifier, Item>(
        id("seed_oak") to PlantableItem(Blocks.OAK_SAPLING, seed_settings),
        id("seed_birch") to PlantableItem(Blocks.BIRCH_SAPLING, seed_settings),
        id("seed_spruce") to PlantableItem(Blocks.SPRUCE_SAPLING, seed_settings),
        id("seed_jungle") to PlantableItem(Blocks.JUNGLE_SAPLING, seed_settings),
        id("seed_acacia") to PlantableItem(Blocks.ACACIA_SAPLING, seed_settings),
        id("seed_dark_oak") to PlantableItem(Blocks.DARK_OAK_SAPLING, seed_settings)
    )

    val CROP_SEEDS = mutableMapOf<Identifier, Item>(
        id("seed_carrot") to PlantableItem(Blocks.CARROTS, seed_settings),
        id("seed_potato") to PlantableItem(Blocks.POTATOES, seed_settings),
        id("seed_sugarcane") to PlantableItem(Blocks.SUGAR_CANE, seed_settings),
        id("seed_cactus") to PlantableItem(Blocks.CACTUS, seed_settings),
        id("seed_chorus") to PlantableItem(Blocks.CHORUS_FLOWER, seed_settings)
    )

    val FLOWER_SEEDS = mutableMapOf<Identifier, Item>(
        // TODO double tall seeds
        //id("seed_sunflower") to PlantableItem(Blocks.SUNFLOWER, seed_settings),
        //id("seed_lilac") to PlantableItem(Blocks.LILAC, seed_settings),
        //id("seed_rose_bush") to PlantableItem(Blocks.ROSE_BUSH, seed_settings),
        //id("seed_peony") to PlantableItem(Blocks.PEONY, seed_settings)
    )

    val OTHER_SEEDS = mutableMapOf<Identifier, Item>(
        id("seed_sea_pickle") to PlantableItem(Blocks.SEA_PICKLE, seed_settings),
        id("seed_grass") to TransformingItem(Blocks.DIRT, Blocks.GRASS_BLOCK, seed_settings),
        id("seed_mycelium") to TransformingItem(Blocks.DIRT, Blocks.MYCELIUM, seed_settings)
    )

    val MESH_STRING = MeshItem(Color.WHITE, mesh_settings)
    val MESH_FLINT = MeshItem(Color.DARK_GRAY, mesh_settings)
    val MESH_IRON = MeshItem(Color.GRAY, mesh_settings)
    val MESH_DIAMOND = MeshItem(Color.AQUA, mesh_settings)

    val MESHES = mutableMapOf<Identifier, Item>(
        id("mesh_string") to MESH_STRING,
        id("mesh_flint") to MESH_FLINT,
        id("mesh_iron") to MESH_IRON,
        id("mesh_diamond") to MESH_DIAMOND
    )

    val RESOURCES = mutableMapOf<Identifier, Item>(
        id("silkworm_raw") to SilkWormItem(Item.Settings().maxCount(64).food(FoodComponents.COD)),
        id("silkworm_cooked") to BaseItem(Item.Settings().maxCount(64).food(FoodComponents.COOKED_COD))
    )

    init {
        val kelpStates = ArrayList<BlockState>()
        for(i in 0..25) {
            kelpStates.add(Blocks.KELP.defaultState.with(KelpBlock.AGE, i))
        }
        CROP_SEEDS[id("seed_kelp")] = object: PlantableItem(kelpStates, seed_settings) {
            override fun placementCheck(context: ItemUsageContext): Boolean {
                return context.world.getFluidState(context.blockPos.offset(context.side)).fluid == Fluids.WATER
            }
        }
    }

    fun registerItems(registry: Registry<Item>) {
        // Register Seeds
        TREE_SEEDS.forEach { (k, v) -> Registry.register(registry, k, v) }
        CROP_SEEDS.forEach { (k, v) -> Registry.register(registry, k, v) }
        OTHER_SEEDS.forEach { (k, v) -> Registry.register(registry, k, v) }
        FLOWER_SEEDS.forEach { (k, v) -> Registry.register(registry, k, v) }

        // Register Meshes
        MESHES.forEach { k, v -> Registry.register(registry, k, v) }

        // Register Others
        RESOURCES.forEach { k, v -> Registry.register(registry, k, v) }
    }
}