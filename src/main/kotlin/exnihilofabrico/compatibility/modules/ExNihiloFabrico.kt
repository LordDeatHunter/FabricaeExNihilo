package exnihilofabrico.compatibility.modules

import exnihilofabrico.api.compatibility.IExNihiloFabricoModule
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.CrucibleRecipe
import exnihilofabrico.api.registry.*
import exnihilofabrico.id
import exnihilofabrico.modules.fluid.FluidInstance
import exnihilofabrico.modules.ore.EnumChunkMaterial
import exnihilofabrico.modules.ore.EnumChunkShape
import exnihilofabrico.modules.ore.EnumPieceShape
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.*
import net.minecraft.block.Blocks
import net.minecraft.fluid.Fluids
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.FluidTags
import net.minecraft.tag.ItemTags
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ExNihiloFabrico: IExNihiloFabricoModule {

    override fun registerBarrelAlchemy(registry: IBarrelAlchemyRegistry) {

    }

    override fun registerBarrelMilking(registry: IBarrelMilkingRegistry) {

    }

    override fun registerCrucibleHeat(registry: ICrucibleHeatRegistry) {
        registry.register(Blocks.TORCH, 1)
        registry.registerFluidTag(FluidTags.LAVA, 3)
        registry.register(Blocks.MAGMA_BLOCK, 4)
        registry.register(Blocks.GLOWSTONE, 2)
    }

    override fun registerCrucibleStone(registry: ICrucibleRegistry) {
        registry.register(CrucibleRecipe(Ingredient.ofItems(Blocks.COBBLESTONE), FluidInstance(Fluids.LAVA.getID(), FluidInstance.BUCKET_AMOUNT/4), true))
    }

    override fun registerCrucibleWood(registry: ICrucibleRegistry) {
        VanillaWoodDefinitions.values().map { it.getLeafBlock() }.forEach {
            CrucibleRecipe(Ingredient.ofItems(it), FluidInstance(Fluids.WATER.getID(), FluidInstance.BUCKET_AMOUNT/4), false)
        }
    }

    override fun registerOres(registry: IOreRegistry) {
        // TODO("Implement tag checking to prevent creation of unnecessary ores")
        // Vanilla Metals
        registry.register("iron", MetalColors.IRON, EnumPieceShape.NORMAL, EnumChunkShape.CHUNK, EnumChunkMaterial.GRANITE)
        registry.register("gold", MetalColors.GOLD, EnumPieceShape.FINE, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)

        // Modded Metals
        registry.register("aluminum",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,   EnumChunkShape.CHUNK, EnumChunkMaterial.SAND)
        registry.register("ardite",    MetalColors.ARDITE,    EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.NETHERRACK)
        registry.register("beryllium", MetalColors.BERYLLIUM, EnumPieceShape.NORMAL, EnumChunkShape.FLINT, EnumChunkMaterial.STONE)
        registry.register("boron",     MetalColors.BORON,     EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.SAND)
        registry.register("cobalt",    MetalColors.COBALT,    EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.NETHERRACK)
        registry.register("copper",    MetalColors.COPPER,    EnumPieceShape.NORMAL, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
        registry.register("lead",      MetalColors.LEAD,      EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
        registry.register("lithium",   MetalColors.LITHIUM,   EnumPieceShape.FINE,   EnumChunkShape.FLINT, EnumChunkMaterial.SAND)
        registry.register("magnesium", MetalColors.MAGNESIUM, EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
        registry.register("nickel",    MetalColors.NICKEL,    EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
        registry.register("silver",    MetalColors.SILVER,    EnumPieceShape.NORMAL, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
        registry.register("tin",       MetalColors.TIN,       EnumPieceShape.NORMAL, EnumChunkShape.LUMP, EnumChunkMaterial.DIORITE)
        registry.register("titanium",  MetalColors.TITANIUM,  EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
        registry.register("thorium",   MetalColors.THORIUM,   EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
        registry.register("tungsten",  MetalColors.TUNGSTEN,  EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.NETHERRACK)
        registry.register("uranium",   MetalColors.URANIUM,   EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
        registry.register("zinc",      MetalColors.ZINC,      EnumPieceShape.FINE,   EnumChunkShape.FLINT, EnumChunkMaterial.ANDESITE)
        registry.register("zirconium", MetalColors.ZIRCONIUM, EnumPieceShape.NORMAL, EnumChunkShape.FLINT, EnumChunkMaterial.ANDESITE)
    }

    override fun registerMesh(registry: IMeshRegistry) {
        registry.register(id("mesh_string"), "item.minecraft.string", Color.WHITE, Identifier("string"))
        registry.register(id("mesh_flint"), "item.minecraft.flint", Color.DARK_GRAY, Identifier("flint"))
        registry.register(id("mesh_iron"), "Iron", Color("777777"), Identifier("iron_ingot"))
        registry.register(id("mesh_gold"), "Gold", Color.GOLD, Identifier("gold_ingot"))
        registry.register(id("mesh_diamond"), "item.minecraft.diamond", Color.DARK_AQUA, Identifier("diamond"))
    }

    override fun registerSieve(registry: ISieveRegistry) {
        val MESH_STRING = Registry.ITEM[id("mesh_string")]
        val MESH_FLINT = Registry.ITEM[id("mesh_flint")]
        val MESH_IRON = Registry.ITEM[id("mesh_iron")]
        val MESH_GOLD = Registry.ITEM[id("mesh_gold")]
        val MESH_DIAMOND = Registry.ITEM[id("mesh_diamond")]

        registry.register(MESH_STRING, Blocks.GRAVEL, Lootable(Items.FLINT, .5))
        registry.register(MESH_FLINT, Blocks.GRAVEL, Lootable(Items.FLINT, .3))

        registry.register(MESH_STRING, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sugarcane"), .1))
        registry.register(MESH_STRING, Fluids.WATER, Blocks.SAND, Lootable(id("seed_kelp"), .05))

        registry.register(MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sugarcane"), .15))
        registry.register(MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(id("seed_kelp"), .1))

        registry.register(MESH_IRON, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sea_pickle"), .1))

        registry.register(MESH_STRING, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, .05))
        registry.register(MESH_STRING, Fluids.WATER, Blocks.DIRT, Lootable(Items.COD, .05))
        registry.register(MESH_STRING, Fluids.WATER, Blocks.DIRT, Lootable(Items.SALMON, .05))

        registry.register(MESH_FLINT, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, .1))
        registry.register(MESH_FLINT, Fluids.WATER, Blocks.DIRT, Lootable(Items.COD, .1))
        registry.register(MESH_FLINT, Fluids.WATER, Blocks.DIRT, Lootable(Items.SALMON, .1))

        registry.register(MESH_IRON, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, .15))
        registry.register(MESH_IRON, Fluids.WATER, Blocks.DIRT, Lootable(Items.COD, .15))
        registry.register(MESH_IRON, Fluids.WATER, Blocks.DIRT, Lootable(Items.SALMON, .15))

        registry.register(MESH_IRON, Fluids.LAVA, Blocks.DIRT, Lootable(Items.COOKED_COD, .15))
        registry.register(MESH_IRON, Fluids.LAVA, Blocks.DIRT, Lootable(Items.COOKED_SALMON, .15))

        registry.register(MESH_STRING, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .01))
        registry.register(MESH_FLINT, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .01))
        registry.register(MESH_GOLD, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .02))
        registry.register(MESH_DIAMOND, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .05))

        registry.register(MESH_STRING, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .01))
        registry.register(MESH_FLINT, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .02))
        registry.register(MESH_IRON, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .05))
        registry.register(MESH_GOLD, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .1))
        registry.register(MESH_DIAMOND, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .2))

        registry.register(MESH_STRING, WitchWaterFluid.Still, Blocks.DIRT, Lootable(id("seed_mycelium"), .01))
        registry.register(MESH_FLINT, WitchWaterFluid.Still, Blocks.DIRT, Lootable(id("seed_mycelium"), .02))
        registry.register(MESH_IRON, WitchWaterFluid.Still, Blocks.DIRT, Lootable(id("seed_mycelium"), .05))
        registry.register(MESH_GOLD, WitchWaterFluid.Still, Blocks.DIRT, Lootable(id("seed_mycelium"), .1))
        registry.register(MESH_DIAMOND, WitchWaterFluid.Still, Blocks.DIRT, Lootable(id("seed_mycelium"), .2))

        registry.register(MESH_STRING, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .01))
        registry.register(MESH_FLINT, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .02))
        registry.register(MESH_IRON, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .05))
        registry.register(MESH_GOLD, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .1))
        registry.register(MESH_DIAMOND, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .2))

        registry.register(MESH_STRING, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .01))
        registry.register(MESH_FLINT, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .02))
        registry.register(MESH_IRON, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .05))
        registry.register(MESH_GOLD, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .1))
        registry.register(MESH_DIAMOND, WitchWaterFluid.Still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .2))

        registry.register(MESH_IRON, Blocks.GRAVEL, Lootable(Items.DIAMOND, .01))
        registry.register(MESH_GOLD, Blocks.GRAVEL, Lootable(Items.DIAMOND, .02))
        registry.register(MESH_DIAMOND, Blocks.GRAVEL, Lootable(Items.DIAMOND, .03))

        for(ore in ExNihiloRegistries.ORES.getAll()) {
            if(!Registry.ITEM.containsId(ore.getPieceID())) continue
            when(ore.material) {
                "iron" -> {
                    registry.register(MESH_STRING, Blocks.GRAVEL, Lootable(ore.getPieceID(), .01))
                    registry.register(MESH_FLINT, Blocks.GRAVEL, Lootable(ore.getPieceID(), .05))
                    registry.register(MESH_IRON, Blocks.GRAVEL, Lootable(ore.getPieceID(), .15))
                    registry.register(MESH_GOLD, Blocks.GRAVEL, Lootable(ore.getPieceID(), .20))
                    registry.register(MESH_DIAMOND, Blocks.GRAVEL, Lootable(ore.getPieceID(), .15))

                    registry.register(MESH_STRING, Blocks.RED_SAND, Lootable(ore.getPieceID(), .05))
                    registry.register(MESH_FLINT, Blocks.RED_SAND, Lootable(ore.getPieceID(), .1))
                    registry.register(MESH_IRON, Blocks.RED_SAND, Lootable(ore.getPieceID(), .2))
                    registry.register(MESH_GOLD, Blocks.RED_SAND, Lootable(ore.getPieceID(), .5, .5))
                    registry.register(MESH_DIAMOND, Blocks.RED_SAND, Lootable(ore.getPieceID(), 1.0))

                    registry.register(MESH_STRING, id("crushed_granite"), Lootable(ore.getPieceID(), .02))
                    registry.register(MESH_FLINT, id("crushed_granite"), Lootable(ore.getPieceID(), .05))
                    registry.register(MESH_IRON, id("crushed_granite"), Lootable(ore.getPieceID(), .1, .1))
                    registry.register(MESH_GOLD, id("crushed_granite"), Lootable(ore.getPieceID(), .6, .6))
                    registry.register(MESH_DIAMOND, id("crushed_granite"), Lootable(ore.getPieceID(), 1.0, .5))
                }
                "gold" -> {
                    registry.register(MESH_IRON, Blocks.GRAVEL, Lootable(ore.getPieceID(), .05))
                    registry.register(MESH_GOLD, Blocks.GRAVEL, Lootable(ore.getPieceID(), .075))
                    registry.register(MESH_DIAMOND, Blocks.GRAVEL, Lootable(ore.getPieceID(), .1))

                    registry.register(MESH_IRON, id("crushed_netherrack"), Lootable(ore.getPieceID(), .1, .1))
                    registry.register(MESH_GOLD, id("crushed_netherrack"), Lootable(ore.getPieceID(), .15, .15))
                    registry.register(MESH_DIAMOND, id("crushed_netherrack"), Lootable(ore.getPieceID(), .2, .2))
                }
                else -> {
                    val sieveable = when(ore.matrix) {
                        EnumChunkMaterial.ANDESITE -> Registry.ITEM.get(id("crushed_andesite"))
                        EnumChunkMaterial.DIORITE -> Registry.ITEM.get(id("crushed_diorite"))
                        EnumChunkMaterial.ENDSTONE -> Registry.ITEM.get(id("crushed_endstone"))
                        EnumChunkMaterial.GRANITE -> Registry.ITEM.get(id("crushed_granite"))
                        EnumChunkMaterial.NETHERRACK -> Registry.ITEM.get(id("crushed_netherrack"))
                        EnumChunkMaterial.PRISMARINE -> Registry.ITEM.get(id("crushed_prismarine"))
                        EnumChunkMaterial.REDSAND -> Blocks.RED_SAND
                        EnumChunkMaterial.SAND -> Blocks.SAND
                        EnumChunkMaterial.SOULSAND -> Blocks.SOUL_SAND
                        EnumChunkMaterial.STONE -> Blocks.GRAVEL
                    }
                    registry.register(MESH_IRON, sieveable, Lootable(ore.getPieceID(), .05))
                    registry.register(MESH_GOLD, sieveable, Lootable(ore.getPieceID(), .075))
                    registry.register(MESH_DIAMOND, sieveable, Lootable(ore.getPieceID(), .1))
                }
            }
        }

        registry.register(MESH_STRING, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .1))
        registry.register(MESH_FLINT, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .2))
        registry.register(MESH_IRON, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .1, .1))
        registry.register(MESH_GOLD, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .2, .2))
        registry.register(MESH_DIAMOND, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .3, .3))
    }

    override fun registerCrook(registry: IToolRegistry) {
        registry.registerDrops(ItemTags.LEAVES, Lootable(Items.STICK, 0.01))
        registry.registerDrops(ItemTags.LEAVES, Lootable(id("silkworm_raw"), 0.1, 0.2, 0.2))
        for(w in VanillaWoodDefinitions.values()) {
            registry.registerDrops(w.getLeafBlock(), Lootable(w.getSeedItem(), 0.25))
        }
    }

    override fun registerHammer(registry: IToolRegistry) {
        // Stone
        registry.registerDrops(Blocks.STONE, Lootable(Blocks.COBBLESTONE, 1.0))
        registry.registerDrops(Blocks.COBBLESTONE, Lootable(Blocks.GRAVEL, 1.0))
        registry.registerDrops(Blocks.GRAVEL, Lootable(Blocks.SAND, 1.0))
        registry.registerDrops(Blocks.SAND, Lootable(id("silt"), 1.0))
        registry.registerDrops(id("silt"), Lootable(id("dust"), 1.0))

        // Andesite
        registry.registerDrops(Blocks.ANDESITE, Lootable(id("crushed_andesite"), 1.0))
        registry.registerDrops(id("crushed_andesite"), Lootable(Blocks.LIGHT_GRAY_CONCRETE_POWDER, 1.0))

        // Diorite
        registry.registerDrops(Blocks.DIORITE, Lootable(id("crushed_diorite"), 1.0))
        registry.registerDrops(id("crushed_diorite"), Lootable(Items.WHITE_CONCRETE_POWDER, 1.0))

        // Granite
        registry.registerDrops(Blocks.GRANITE, Lootable(id("crushed_granite"), 1.0))
        registry.registerDrops(id("crushed_granite"), Lootable(Items.RED_SAND, 1.0))

        // Netherrack
        registry.registerDrops(Blocks.NETHERRACK, Lootable(id("crushed_netherrack"), 1.0))
        registry.registerDrops(Blocks.NETHER_BRICKS, Lootable(id("crushed_netherrack"), 1.0))
        registry.registerDrops(id("crushed_netherrack"), Lootable(Blocks.RED_CONCRETE_POWDER, 1.0))

        // End Stone
        registry.registerDrops(Blocks.END_STONE, Lootable(id("crushed_endstone"), 1.0))
        registry.registerDrops(Blocks.END_STONE_BRICKS, Lootable(id("crushed_endstone"), 1.0))
        registry.registerDrops(id("crushed_endstone"), Lootable(Blocks.YELLOW_CONCRETE_POWDER, 1.0))

        // Prismarine
        registry.registerDrops(Blocks.PRISMARINE, Lootable(id("crushed_prismarine"), 1.0))
        registry.registerDrops(id("crushed_prismarine"), Lootable(Blocks.CYAN_CONCRETE_POWDER, 1.0))

        // Misc.
        registry.registerDrops(ItemTags.WOOL, Lootable(Items.STRING.asStack(4), 1.0))
        EnumVanillaColors.values().forEach { c ->
            // Concrete Hammering
            registry.registerDrops(c.getConcrete(), Lootable(c.getConcretePowder(), 1.0))
            registry.registerDrops(c.getConcretePowder(), Lootable(id("silt"), 1.0))
            registry.registerDrops(c.getConcretePowder(), Lootable(c.getDye(), 0.0625))
            // Wool Hammering
            registry.registerDrops(c.getWool(), Lootable(c.getDye(), 0.5))
            // Glass Hammering
            registry.registerDrops(c.getGlass(), Lootable(Blocks.SAND, 1.0))
            registry.registerDrops(c.getGlass(), Lootable(c.getDye(), 0.0625))
        }

        //
    }

    override fun registerWitchWaterFluid(registry: IWitchWaterFluidRegistry) {
        registry.register(FluidIngredient(Fluids.WATER, Fluids.FLOWING_WATER), WeightedList(mutableMapOf(
            Blocks.DIRT to 51,
            Blocks.GRASS_BLOCK to 12,
            Blocks.COARSE_DIRT to 12,
            Blocks.MYCELIUM to 12,
            Blocks.PODZOL to 12,
            Blocks.PRISMARINE to 1)
        ))
        registry.register(FluidIngredient(FluidTags.LAVA), WeightedList(mutableMapOf(
            Blocks.COBBLESTONE to 3,
            Blocks.ANDESITE to 1,
            Blocks.DIORITE to 1,
            Blocks.GRANITE to 1)))

    }

}