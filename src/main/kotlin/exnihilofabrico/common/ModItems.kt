package exnihilofabrico.common

import exnihilofabrico.ExNihiloConfig
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.common.base.BaseItem
import exnihilofabrico.common.farming.PlantableItem
import exnihilofabrico.common.farming.SilkWormItem
import exnihilofabrico.common.farming.TransformingItem
import exnihilofabrico.common.fluids.WitchWaterFluid
import exnihilofabrico.common.sieves.MeshItem
import exnihilofabrico.common.sieves.MeshProperties
import exnihilofabrico.id
import exnihilofabrico.registry.MeshRegistry
import exnihilofabrico.util.Color
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.KelpBlock
import net.minecraft.fluid.Fluids
import net.minecraft.item.FoodComponents
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModItems {
    val seed_settings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(64)

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
        ExNihiloRegistries.MESH.registerItems(registry)

        // Register Others
        RESOURCES.forEach { k, v -> Registry.register(registry, k, v) }

        // Register Ores
        ExNihiloRegistries.ORES.registerPieceItems(registry)
        ExNihiloRegistries.ORES.registerChunkItems(registry)
        ExNihiloRegistries.ORES.registerItemTags()

        // Register Buckets
        Registry.register(registry, id("witchwater_bucket"), WitchWaterFluid.Bucket)
    }
}