package exnihilofabrico.compatibility.modules

import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import exnihilofabrico.api.compatibility.IExNihiloFabricoModule
import exnihilofabrico.api.crafting.FluidIngredient
import exnihilofabrico.api.crafting.ItemIngredient
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.crafting.WeightedList
import exnihilofabrico.api.recipes.barrel.AlchemyRecipe
import exnihilofabrico.api.registry.*
import exnihilofabrico.id
import exnihilofabrico.modules.ModTags
import exnihilofabrico.modules.barrels.modes.FluidMode
import exnihilofabrico.modules.fluids.BloodFluid
import exnihilofabrico.modules.fluids.BrineFluid
import exnihilofabrico.modules.fluids.MilkFluid
import exnihilofabrico.modules.ore.EnumChunkMaterial
import exnihilofabrico.modules.ore.EnumChunkShape
import exnihilofabrico.modules.ore.EnumPieceShape
import exnihilofabrico.modules.witchwater.WitchWaterFluid
import exnihilofabrico.util.*
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.fluid.Fluids
import net.minecraft.item.Items
import net.minecraft.item.ToolMaterials
import net.minecraft.tag.FluidTags
import net.minecraft.tag.ItemTags
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.village.VillagerProfession

object ExNihiloFabrico: IExNihiloFabricoModule {

    override fun registerAlchemy(registry: IAlchemyRegistry) {
        registry.register(Fluids.WATER, getExNihiloItem("seed_mycelium"), WitchWaterFluid.still)

        registry.register(Fluids.LAVA, Items.GLOWSTONE_DUST, Blocks.END_STONE)
        registry.register(Fluids.LAVA, Items.REDSTONE, Blocks.NETHERRACK)
        registry.register(Fluids.WATER, getExNihiloBlock("dust"), Blocks.CLAY)
        registry.register(Fluids.WATER, getExNihiloBlock("silt"), Blocks.CLAY)

        registry.register(MilkFluid.tag, Items.BROWN_MUSHROOM, Blocks.SLIME_BLOCK)
        registry.register(MilkFluid.tag, Items.RED_MUSHROOM, Blocks.SLIME_BLOCK)

        registry.register(BloodFluid.tag, Blocks.SAND, Blocks.RED_SAND)
        registry.register(BloodFluid.tag, Blocks.SANDSTONE, Blocks.RED_SANDSTONE)
        registry.register(BloodFluid.tag, Blocks.CHISELED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE)
        registry.register(BloodFluid.tag, Blocks.CUT_SANDSTONE, Blocks.CUT_RED_SANDSTONE)
        registry.register(BloodFluid.tag, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE)

        registry.register(AlchemyRecipe(FluidIngredient(Fluids.WATER),
                                        ItemIngredient(getExNihiloItem("salt_bottle")),
                                        FluidMode(FluidVolume.create(BrineFluid.still, FluidVolume.BUCKET)),
                                        Lootable(Items.GLASS_BOTTLE.asStack(), 1.0)))

        registry.register(AlchemyRecipe(FluidIngredient(Fluids.WATER),
            ItemIngredient(TagRegistry.item(Identifier("c:salt"))),
            FluidMode(FluidVolume.create(BrineFluid.still, FluidVolume.BUCKET))))

        registry.register(BrineFluid.still, Items.BLUE_DYE, Blocks.TUBE_CORAL_BLOCK)
        registry.register(BrineFluid.still, Items.PINK_DYE, Blocks.BRAIN_CORAL_BLOCK)
        registry.register(BrineFluid.still, Items.MAGENTA_DYE, Blocks.BUBBLE_CORAL_BLOCK)
        registry.register(BrineFluid.still, Items.RED_DYE, Blocks.FIRE_CORAL_BLOCK)
        registry.register(BrineFluid.still, Items.YELLOW_DYE, Blocks.HORN_CORAL_BLOCK)

        registry.register(FluidIngredient(BrineFluid.still), ItemIngredient(ItemTags.WOOL), Blocks.WET_SPONGE.asStack())
    }

    override fun registerCompost(registry: ICompostRegistry) {
        registry.register(ItemTags.LEAVES, Blocks.DIRT, 0.125, Color.DARK_GREEN)
        registry.register(ItemTags.SAPLINGS, Blocks.DIRT, 0.0625, Color.DARK_GREEN)

        registry.register(Items.CHORUS_FLOWER, Blocks.END_STONE, 0.25, Color.DARK_PURPLE)
        registry.register(Items.POPPED_CHORUS_FRUIT, Blocks.END_STONE, 0.125, Color.LIGHT_PURPLE)
        registry.register(Items.CHORUS_FRUIT, Blocks.END_STONE, 0.0625, Color.LIGHT_PURPLE)

        registry.register(Items.COBWEB, Blocks.WHITE_WOOL, 0.5, Color.WHITE)

        registry.register(Items.CACTUS, Blocks.DIRT, 0.0625, Color.DARK_GREEN)

        registry.register(TagRegistry.item(Identifier("c:seeds")), Blocks.DIRT, 0.0625, Color.GREEN)
        registry.register(TagRegistry.item(Identifier("c:veggies")), Blocks.DIRT, 0.0625, Color.YELLOW)
        registry.register(ItemTags.SMALL_FLOWERS, Blocks.DIRT, 0.0625, Color.RED)
        registry.register(TagRegistry.item(Identifier("c:dyes")), Blocks.DIRT, 0.125, Color.RED)
        registry.register(TagRegistry.item(Identifier("c:raw_meat")), Blocks.DIRT, 0.125, Color.RED)
        registry.register(TagRegistry.item(Identifier("c:cooked_meat")), Blocks.DIRT, 0.25, Color.RED)
    }

    override fun registerLeaking(registry: ILeakingRegistry) {
        registry.register(Blocks.COBBLESTONE, Fluids.WATER, FluidVolume.BUCKET / 10, Blocks.MOSSY_COBBLESTONE)
        registry.register(Blocks.STONE_BRICKS, Fluids.WATER, FluidVolume.BUCKET / 10, Blocks.MOSSY_STONE_BRICKS)

        registry.register(ItemTags.SAPLINGS, WitchWaterFluid.still, FluidVolume.BUCKET / 10, Blocks.DEAD_BUSH)
        registry.register(Blocks.GRAVEL, WitchWaterFluid.still, FluidVolume.BUCKET / 10, getExNihiloBlock("crushed_netherrack"))
        registry.register(Blocks.SAND, WitchWaterFluid.still, FluidVolume.BUCKET / 2, Blocks.SOUL_SAND)
        registry.register(Blocks.PODZOL, WitchWaterFluid.still, FluidVolume.BUCKET / 2, Blocks.MYCELIUM)
        registry.register(ItemTags.SMALL_FLOWERS, WitchWaterFluid.still, FluidVolume.BUCKET / 2, Blocks.BROWN_MUSHROOM)

        registry.register(Blocks.COBBLESTONE, BloodFluid.tag, FluidVolume.BUCKET / 10, Blocks.NETHERRACK)
        registry.register(Blocks.GRAVEL, BloodFluid.tag, FluidVolume.BUCKET / 20, getExNihiloBlock("crushed_netherrack"))
    }

    override fun registerMilking(registry: IMilkingRegistry) {
        registry.register(EntityType.COW, MilkFluid.still, FluidVolume.BUCKET / 100, 20)
        registry.register(EntityType.WITCH, WitchWaterFluid.still, FluidVolume.BUCKET / 100, 20)
    }

    override fun registerCrucibleHeat(registry: ICrucibleHeatRegistry) {
        registry.register(Blocks.TORCH, 1)
        registry.register(FluidTags.LAVA, 3)
        registry.register(Blocks.MAGMA_BLOCK, 4)
        registry.register(Blocks.GLOWSTONE, 2)
    }

    override fun registerCrucibleStone(registry: ICrucibleRegistry) {
        registry.register(Blocks.NETHERRACK, Fluids.LAVA,FluidVolume.BUCKET / 2)
        registry.register(Blocks.COBBLESTONE, Fluids.LAVA,FluidVolume.BUCKET / 4)
        registry.register(Blocks.GRAVEL, Fluids.LAVA,FluidVolume.BUCKET / 8)
        registry.register(ItemTags.SAND, Fluids.LAVA,FluidVolume.BUCKET / 16)
    }

    override fun registerCrucibleWood(registry: ICrucibleRegistry) {
        registry.register(ItemTags.SAPLINGS, Fluids.WATER, FluidVolume.BUCKET / 10)
        registry.register(ItemTags.LEAVES, Fluids.WATER, FluidVolume.BUCKET / 4)
        registry.register(ItemTags.SMALL_FLOWERS, Fluids.WATER, FluidVolume.BUCKET / 10)
        registry.register(TagRegistry.item(Identifier("c:seeds")), Fluids.WATER, FluidVolume.BUCKET / 10)
        registry.register(TagRegistry.item(Identifier("c:veggies")), Fluids.WATER, FluidVolume.BUCKET / 10)
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
        registry.register(
            id("mesh_string"),
            ToolMaterials.WOOD.enchantability,
            "item.minecraft.string",
            Color.WHITE,
            Identifier("string"))
        registry.register(
            id("mesh_flint"),
            ToolMaterials.STONE.enchantability,
            "item.minecraft.flint",
            Color.DARK_GRAY,
            Identifier("flint"))
        registry.register(
            id("mesh_iron"),
            ToolMaterials.IRON.enchantability,
            "Iron",
            Color("777777"),
            Identifier("iron_ingot"))
        registry.register(
            id("mesh_gold"),
            ToolMaterials.GOLD.enchantability,
            "Gold",
            Color.GOLD,
            Identifier("gold_ingot"))
        registry.register(
            id("mesh_diamond"),
            ToolMaterials.DIAMOND.enchantability,
            "item.minecraft.diamond",
            Color.DARK_AQUA,
            Identifier("diamond"))
    }

    override fun registerSieve(registry: ISieveRegistry) {
        val stringMesh = Registry.ITEM[id("mesh_string")]
        val flintMesh = Registry.ITEM[id("mesh_flint")]
        val ironMesh = Registry.ITEM[id("mesh_iron")]
        val goldMesh = Registry.ITEM[id("mesh_gold")]
        val diamondMesh = Registry.ITEM[id("mesh_diamond")]

        registry.register(stringMesh, Blocks.GRAVEL, Lootable(Items.FLINT, .5))
        registry.register(flintMesh, Blocks.GRAVEL, Lootable(Items.FLINT, .3))

        registry.register(stringMesh, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sugarcane"), .1))
        registry.register(stringMesh, Fluids.WATER, Blocks.SAND, Lootable(id("seed_kelp"), .05))

        registry.register(flintMesh, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sugarcane"), .15))
        registry.register(flintMesh, Fluids.WATER, Blocks.SAND, Lootable(id("seed_kelp"), .1))

        registry.register(ironMesh, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sea_pickle"), .1))

        registry.register(stringMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, .05))
        registry.register(stringMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.COD, .05))
        registry.register(stringMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.SALMON, .05))

        registry.register(flintMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, .1))
        registry.register(flintMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.COD, .1))
        registry.register(flintMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.SALMON, .1))

        registry.register(ironMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, .15))
        registry.register(ironMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.COD, .15))
        registry.register(ironMesh, Fluids.WATER, Blocks.DIRT, Lootable(Items.SALMON, .15))

        registry.register(ironMesh, Fluids.LAVA, Blocks.DIRT, Lootable(Items.COOKED_COD, .15))
        registry.register(ironMesh, Fluids.LAVA, Blocks.DIRT, Lootable(Items.COOKED_SALMON, .15))

        registry.register(stringMesh, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .01))
        registry.register(flintMesh, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .01))
        registry.register(goldMesh, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .02))
        registry.register(diamondMesh, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, .05))

        registry.register(stringMesh, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .01))
        registry.register(flintMesh, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .02))
        registry.register(ironMesh, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .05))
        registry.register(goldMesh, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .1))
        registry.register(diamondMesh, Fluids.WATER, Blocks.GRAVEL, Lootable(Items.PRISMARINE_SHARD, .2))

        registry.register(stringMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(id("seed_mycelium"), .01))
        registry.register(flintMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(id("seed_mycelium"), .02))
        registry.register(ironMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(id("seed_mycelium"), .05))
        registry.register(goldMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(id("seed_mycelium"), .1))
        registry.register(diamondMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(id("seed_mycelium"), .2))

        registry.register(stringMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .01))
        registry.register(flintMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .02))
        registry.register(ironMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .05))
        registry.register(goldMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .1))
        registry.register(diamondMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.RED_MUSHROOM, .2))

        registry.register(stringMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .01))
        registry.register(flintMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .02))
        registry.register(ironMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .05))
        registry.register(goldMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .1))
        registry.register(diamondMesh, WitchWaterFluid.still, Blocks.DIRT, Lootable(Items.BROWN_MUSHROOM, .2))

        registry.register(ironMesh, Blocks.GRAVEL, Lootable(Items.DIAMOND, .01))
        registry.register(goldMesh, Blocks.GRAVEL, Lootable(Items.DIAMOND, .02))
        registry.register(diamondMesh, Blocks.GRAVEL, Lootable(Items.DIAMOND, .03))

        registry.register(flintMesh, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 0.2))
        registry.register(ironMesh, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 0.3))
        registry.register(goldMesh, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 0.4))
        registry.register(diamondMesh, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 0.5))

        registry.register(flintMesh, Fluids.WATER, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.2))
        registry.register(ironMesh, Fluids.WATER, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.3))
        registry.register(goldMesh, Fluids.WATER, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.4))
        registry.register(diamondMesh, Fluids.WATER, getExNihiloBlock("crushed_prismarine"), Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.5))


        for(ore in ExNihiloRegistries.ORES.getAll()) {
            if(!Registry.ITEM.containsId(ore.getPieceID())) continue
            when(ore.material) {
                "iron" -> {
                    registry.register(stringMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .01))
                    registry.register(flintMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .05))
                    registry.register(ironMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .15))
                    registry.register(goldMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .20))
                    registry.register(diamondMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .15))

                    registry.register(stringMesh, Blocks.RED_SAND, Lootable(ore.getPieceID(), .05))
                    registry.register(flintMesh, Blocks.RED_SAND, Lootable(ore.getPieceID(), .1))
                    registry.register(ironMesh, Blocks.RED_SAND, Lootable(ore.getPieceID(), .2))
                    registry.register(goldMesh, Blocks.RED_SAND, Lootable(ore.getPieceID(), .5, .5))
                    registry.register(diamondMesh, Blocks.RED_SAND, Lootable(ore.getPieceID(), 1.0))

                    registry.register(stringMesh, id("crushed_granite"), Lootable(ore.getPieceID(), .02))
                    registry.register(flintMesh, id("crushed_granite"), Lootable(ore.getPieceID(), .05))
                    registry.register(ironMesh, id("crushed_granite"), Lootable(ore.getPieceID(), .1, .1))
                    registry.register(goldMesh, id("crushed_granite"), Lootable(ore.getPieceID(), .6, .6))
                    registry.register(diamondMesh, id("crushed_granite"), Lootable(ore.getPieceID(), 1.0, .5))
                }
                "gold" -> {
                    registry.register(ironMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .05))
                    registry.register(goldMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .075))
                    registry.register(diamondMesh, Blocks.GRAVEL, Lootable(ore.getPieceID(), .1))

                    registry.register(ironMesh, id("crushed_netherrack"), Lootable(ore.getPieceID(), .1, .1))
                    registry.register(goldMesh, id("crushed_netherrack"), Lootable(ore.getPieceID(), .15, .15))
                    registry.register(diamondMesh, id("crushed_netherrack"), Lootable(ore.getPieceID(), .2, .2))
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
                    registry.register(ironMesh, sieveable, Lootable(ore.getPieceID(), .05))
                    registry.register(goldMesh, sieveable, Lootable(ore.getPieceID(), .075))
                    registry.register(diamondMesh, sieveable, Lootable(ore.getPieceID(), .1))
                }
            }
        }

        registry.register(stringMesh, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .1))
        registry.register(flintMesh, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .2))
        registry.register(ironMesh, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .1, .1))
        registry.register(goldMesh, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .2, .2))
        registry.register(diamondMesh, Fluids.LAVA, id("crushed_granite"), Lootable(Items.IRON_NUGGET, .3, .3))
    }

    override fun registerCrook(registry: IToolRegistry) {
        registry.register(ItemTags.LEAVES, Lootable(Items.STICK, 0.01))
        registry.register(ItemTags.LEAVES, Lootable(id("silkworm_raw"), 0.1, 0.2, 0.2))
        ModTags.INFESTED_LEAVES?.let { registry.register(it, Lootable(Items.STRING.asStack(1), 1.0, 1.0, 0.5, 0.2, 0.1)) }
        for(w in VanillaWoodDefinitions.values()) {
            registry.register(w.getLeafBlock(), Lootable(w.getSeedItem(), 0.25))
        }
    }

    override fun registerHammer(registry: IToolRegistry) {
        // Stone
        registry.register(Blocks.STONE, Lootable(Blocks.COBBLESTONE, 1.0))
        registry.register(Blocks.COBBLESTONE, Lootable(Blocks.GRAVEL, 1.0, 0.25))
        registry.register(Blocks.GRAVEL, Lootable(Blocks.SAND, 1.0, 0.25))
        registry.register(Blocks.SAND, Lootable(id("silt"), 1.0, 0.25))
        registry.register(id("silt"), Lootable(id("dust"), 1.0, 0.25))

        // Andesite
        registry.register(Blocks.ANDESITE, Lootable(id("crushed_andesite"), 1.0, 0.5))
        registry.register(id("crushed_andesite"), Lootable(Blocks.LIGHT_GRAY_CONCRETE_POWDER, 1.0, 0.5))

        // Diorite
        registry.register(Blocks.DIORITE, Lootable(id("crushed_diorite"), 1.0, 0.5))
        registry.register(id("crushed_diorite"), Lootable(Items.WHITE_CONCRETE_POWDER, 1.0, 0.5))

        // Granite
        registry.register(Blocks.GRANITE, Lootable(id("crushed_granite"), 1.0, 0.5))
        registry.register(id("crushed_granite"), Lootable(Items.RED_SAND, 1.0, 0.5))

        // Netherrack
        registry.register(Blocks.NETHERRACK, Lootable(id("crushed_netherrack"), 1.0, 0.5))
        registry.register(Blocks.NETHER_BRICKS, Lootable(id("crushed_netherrack"), 1.0, 0.5))
        registry.register(id("crushed_netherrack"), Lootable(Blocks.RED_CONCRETE_POWDER, 1.0, 0.5))

        // End Stone
        registry.register(Blocks.END_STONE, Lootable(id("crushed_endstone"), 1.0))
        registry.register(Blocks.END_STONE_BRICKS, Lootable(id("crushed_endstone"), 1.0, 0.5))
        registry.register(id("crushed_endstone"), Lootable(Blocks.YELLOW_CONCRETE_POWDER, 1.0, 0.5))

        // Prismarine
        registry.register(Blocks.PRISMARINE, Lootable(id("crushed_prismarine"), 1.0))
        registry.register(id("crushed_prismarine"), Lootable(Blocks.CYAN_CONCRETE_POWDER, 1.0, 0.5))

        // Misc.
        registry.register(ItemTags.WOOL, Lootable(Items.STRING.asStack(4), 1.0))
        EnumVanillaColors.values().forEach { c ->
            // Concrete Hammering
            registry.register(c.getConcrete(), Lootable(c.getConcretePowder(), 1.0))
            registry.register(c.getConcretePowder(), Lootable(id("silt"), 1.0))
            registry.register(c.getConcretePowder(), Lootable(c.getDye(), 0.0625))
            // Wool Hammering
            registry.register(c.getWool(), Lootable(c.getDye(), 0.5))
            // Glass Hammering
            registry.register(c.getGlass(), Lootable(Blocks.SAND, 1.0))
            registry.register(c.getGlass(), Lootable(c.getDye(), 0.0625))
        }

        // Corals
        registry.register(Blocks.TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL, 1.0, 1.0, 0.5, 0.1)
        registry.register(Blocks.BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL, 1.0, 1.0, 0.5, 0.1)
        registry.register(Blocks.BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL, 1.0, 1.0, 0.5, 0.1)
        registry.register(Blocks.FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL, 1.0, 1.0, 0.5, 0.1)
        registry.register(Blocks.HORN_CORAL_BLOCK, Blocks.HORN_CORAL, 1.0, 1.0, 0.5, 0.1)

        registry.register(Blocks.TUBE_CORAL, Blocks.TUBE_CORAL_FAN, 1.0, 0.5)
        registry.register(Blocks.BRAIN_CORAL, Blocks.BRAIN_CORAL_FAN, 1.0, 0.5)
        registry.register(Blocks.BUBBLE_CORAL, Blocks.BUBBLE_CORAL_FAN, 1.0, 0.5)
        registry.register(Blocks.FIRE_CORAL, Blocks.FIRE_CORAL_FAN, 1.0, 0.5)
        registry.register(Blocks.HORN_CORAL, Blocks.HORN_CORAL_FAN, 1.0, 0.5)
    }

    override fun registerWitchWaterWorld(registry: IWitchWaterWorldRegistry) {
        registry.register(
            FluidIngredient(Fluids.WATER, Fluids.FLOWING_WATER), WeightedList(
                mutableMapOf(
                    Blocks.DIRT to 51,
                    Blocks.GRASS_BLOCK to 12,
                    Blocks.COARSE_DIRT to 12,
                    Blocks.MYCELIUM to 12,
                    Blocks.PODZOL to 12
                )
            )
        )
        registry.register(FluidIngredient(FluidTags.LAVA), WeightedList(
            mutableMapOf(
                Blocks.COBBLESTONE to 3,
                Blocks.ANDESITE to 1,
                Blocks.DIORITE to 1,
                Blocks.GRANITE to 1
            )
        ))
        registry.register(FluidIngredient(TagRegistry.fluid(Identifier("c:brine"))), WeightedList(
            mutableMapOf(
                Blocks.DEAD_BRAIN_CORAL_BLOCK to 16,
                Blocks.DEAD_BUBBLE_CORAL_BLOCK to 16,
                Blocks.DEAD_FIRE_CORAL_BLOCK to 16,
                Blocks.DEAD_HORN_CORAL_BLOCK to 16,
                Blocks.DEAD_TUBE_CORAL_BLOCK to 16,
                Blocks.BRAIN_CORAL_BLOCK to 4,
                Blocks.BUBBLE_CORAL_BLOCK to 4,
                Blocks.FIRE_CORAL_BLOCK to 4,
                Blocks.HORN_CORAL_BLOCK to 4,
                Blocks.TUBE_CORAL_BLOCK to 4,
                Blocks.PRISMARINE to 1
            )
        ))
        registry.register(FluidIngredient(TagRegistry.fluid(Identifier("c:blood"))), WeightedList(
            mutableMapOf(
                Blocks.NETHERRACK to 1,
                Blocks.RED_SAND to 16,
                Blocks.FIRE_CORAL_BLOCK to 4
            )
        ))
    }

    override fun registerWitchWaterEntity(registry: IWitchWaterEntityRegistry) {
        registry.register(EntityType.SKELETON, EntityType.WITHER_SKELETON)
        registry.register(EntityType.SLIME, EntityType.MAGMA_CUBE)
        registry.register(EntityType.SPIDER, EntityType.CAVE_SPIDER)

        registry.register(EntityType.COW, EntityType.MOOSHROOM)
        registry.register(EntityType.PIG, EntityType.ZOMBIE_PIGMAN)
        registry.register(EntityType.CHICKEN, EntityType.VEX)
        registry.register(EntityType.SQUID, EntityType.GHAST)
        registry.register(EntityType.PANDA, EntityType.RAVAGER)
        registry.register(EntityType.POLAR_BEAR, EntityType.RAVAGER)
        registry.register(EntityType.HORSE, EntityType.SKELETON_HORSE)
        registry.register(EntityType.DONKEY, EntityType.ZOMBIE_HORSE)
        registry.register(EntityType.MULE, EntityType.ZOMBIE_HORSE)
        registry.register(EntityType.BAT, EntityType.PHANTOM)
        registry.register(EntityType.PARROT, EntityType.PHANTOM)
        registry.register(EntityType.TURTLE, EntityType.SHULKER)

        registry.register(EntityType.PUFFERFISH, EntityType.GUARDIAN)
        registry.register(EntityType.SALMON, EntityType.SILVERFISH)
        registry.register(EntityType.TROPICAL_FISH, EntityType.SILVERFISH)
        registry.register(EntityType.COD, EntityType.SILVERFISH)

        /**
         * Villagers
         */
        registry.register(EntityType.VILLAGER, VillagerProfession.ARMORER, EntityType.PILLAGER)
        registry.register(EntityType.VILLAGER, VillagerProfession.BUTCHER, EntityType.VINDICATOR)
        registry.register(EntityType.VILLAGER, VillagerProfession.CARTOGRAPHER, EntityType.PILLAGER)
        registry.register(EntityType.VILLAGER, VillagerProfession.CLERIC, EntityType.EVOKER)
        registry.register(EntityType.VILLAGER, VillagerProfession.FARMER, EntityType.HUSK)
        registry.register(EntityType.VILLAGER, VillagerProfession.FISHERMAN, EntityType.DROWNED)
        registry.register(EntityType.VILLAGER, VillagerProfession.FLETCHER, EntityType.STRAY)
        registry.register(EntityType.VILLAGER, VillagerProfession.LEATHERWORKER, EntityType.PILLAGER)
        registry.register(EntityType.VILLAGER, VillagerProfession.LIBRARIAN, EntityType.ILLUSIONER)
        registry.register(EntityType.VILLAGER, VillagerProfession.MASON, EntityType.PILLAGER)
        registry.register(EntityType.VILLAGER, VillagerProfession.NITWIT, EntityType.ZOMBIE_VILLAGER)
        registry.register(EntityType.VILLAGER, VillagerProfession.SHEPHERD, EntityType.PILLAGER)
        registry.register(EntityType.VILLAGER, VillagerProfession.TOOLSMITH, EntityType.PILLAGER)
        registry.register(EntityType.VILLAGER, VillagerProfession.WEAPONSMITH, EntityType.PILLAGER)
        // Do generic last.
        registry.register(EntityType.VILLAGER, VillagerProfession.NONE, EntityType.ZOMBIE_VILLAGER)

    }

    override fun registerFluidOnTop(registry: IFluidOnTopRegistry) {
        registry.register(Fluids.LAVA, Fluids.WATER, Blocks.OBSIDIAN)
        registry.register(Fluids.WATER, Fluids.LAVA, Blocks.STONE)
    }

    override fun registerFluidTransform(registry: IFluidTransformRegistry) {
        registry.register(Fluids.WATER, Blocks.MYCELIUM, WitchWaterFluid.still)
        registry.register(MilkFluid.still, Blocks.MYCELIUM, Blocks.SLIME_BLOCK)
    }
}
