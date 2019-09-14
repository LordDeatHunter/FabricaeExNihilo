package exnihilofabrico.modules

import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.registry.*
import exnihilofabrico.common.ModItems
import exnihilofabrico.common.ore.EnumChunkMaterial
import exnihilofabrico.common.ore.EnumChunkShape
import exnihilofabrico.common.ore.EnumPieceShape
import exnihilofabrico.id
import exnihilofabrico.util.EnumVanillaColors
import exnihilofabrico.util.EnumVanillaWoodTypes
import exnihilofabrico.util.MetalColors
import exnihilofabrico.util.asStack
import net.minecraft.block.Blocks
import net.minecraft.fluid.Fluids
import net.minecraft.item.Items
import net.minecraft.tag.BlockTags

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

    override fun registerOres(registry: IOreRegistry) {
//        // TODO("Implement tag checking to prevent creation of unnecessary ores")
//        // Vanilla Metals
//        registry.register("iron", MetalColors.IRON, EnumPieceShape.NORMAL, EnumChunkShape.CHUNK, EnumChunkMaterial.GRANITE)
//        registry.register("gold", MetalColors.GOLD, EnumPieceShape.FINE, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
//
//        // Modded Metals
//        registry.register("aluminum",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,   EnumChunkShape.CHUNK, EnumChunkMaterial.SAND)
//        registry.register("ardite",    MetalColors.ARDITE,    EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.NETHERRACK)
//        registry.register("beryllium", MetalColors.BERYLLIUM, EnumPieceShape.NORMAL, EnumChunkShape.FLINT, EnumChunkMaterial.STONE)
//        registry.register("boron",     MetalColors.BORON,     EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.SAND)
//        registry.register("cobalt",    MetalColors.COBALT,    EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.NETHERRACK)
//        registry.register("copper",    MetalColors.COPPER,    EnumPieceShape.NORMAL, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
//        registry.register("lead",      MetalColors.LEAD,      EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
//        registry.register("lithium",   MetalColors.LITHIUM,   EnumPieceShape.FINE,   EnumChunkShape.FLINT, EnumChunkMaterial.SAND)
//        registry.register("magnesium", MetalColors.MAGNESIUM, EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
//        registry.register("nickel",    MetalColors.NICKEL,    EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
//        registry.register("silver",    MetalColors.SILVER,    EnumPieceShape.NORMAL, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
//        registry.register("tin",       MetalColors.TIN,       EnumPieceShape.NORMAL, EnumChunkShape.LUMP, EnumChunkMaterial.DIORITE)
//        registry.register("titanium",  MetalColors.TITANIUM,  EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
//        registry.register("thorium",   MetalColors.THORIUM,   EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
//        registry.register("tungsten",  MetalColors.TUNGSTEN,  EnumPieceShape.COARSE, EnumChunkShape.CHUNK, EnumChunkMaterial.ANDESITE)
//        registry.register("uranium",   MetalColors.URANIUM,   EnumPieceShape.COARSE, EnumChunkShape.LUMP, EnumChunkMaterial.STONE)
//        registry.register("zinc",      MetalColors.ZINC,      EnumPieceShape.FINE,   EnumChunkShape.FLINT, EnumChunkMaterial.ANDESITE)
//        registry.register("zirconium", MetalColors.ZIRCONIUM, EnumPieceShape.NORMAL, EnumChunkShape.FLINT, EnumChunkMaterial.ANDESITE)

        // Test Materials
        registry.register("00",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.ANDESITE)
        registry.register("01",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.ANDESITE)
        registry.register("02",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.ANDESITE)
        registry.register("10",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.DIORITE)
        registry.register("11",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.DIORITE)
        registry.register("12",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.DIORITE)
        registry.register("20",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.ENDSTONE)
        registry.register("21",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.ENDSTONE)
        registry.register("22",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.ENDSTONE)
        registry.register("30",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.GRANITE)
        registry.register("31",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.GRANITE)
        registry.register("32",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.GRANITE)
        registry.register("40",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.NETHERRACK)
        registry.register("41",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.NETHERRACK)
        registry.register("42",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.NETHERRACK)
        registry.register("50",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.PRISMARINE)
        registry.register("51",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.PRISMARINE)
        registry.register("52",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.PRISMARINE)
        registry.register("60",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.REDSAND)
        registry.register("61",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.REDSAND)
        registry.register("62",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.REDSAND)
        registry.register("70",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.SAND)
        registry.register("71",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.SAND)
        registry.register("72",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.SAND)
        registry.register("80",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.SOULSAND)
        registry.register("81",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.SOULSAND)
        registry.register("82",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.SOULSAND)
        registry.register("90",  MetalColors.ALUMINUM,  EnumPieceShape.COARSE,   EnumChunkShape.CHUNK, EnumChunkMaterial.STONE)
        registry.register("91",  MetalColors.ALUMINUM,  EnumPieceShape.NORMAL,   EnumChunkShape.FLINT, EnumChunkMaterial.STONE)
        registry.register("92",  MetalColors.ALUMINUM,  EnumPieceShape.FINE,     EnumChunkShape.LUMP,  EnumChunkMaterial.STONE)
    }

    override fun registerSieve(registry: ISieveRegistry) {
        registry.register(ModItems.MESH_STRING, Blocks.GRAVEL, Lootable(Items.FLINT, .5))
        registry.register(ModItems.MESH_FLINT, Blocks.GRAVEL, Lootable(Items.FLINT, .3))

        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sugarcane"), listOf(.5)))
        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.SAND, Lootable(id("seed_kelp"), listOf(.3, .2)))
        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, listOf(.1, .2, .3)))
        registry.register(ModItems.MESH_STRING, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, listOf(.1, .2, .3)))

        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sugarcane"), listOf(.3)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(id("seed_kelp"), listOf(.5, .5)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.SAND, Lootable(id("seed_sea_pickle"), listOf(.1)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.DIRT, Lootable(Items.TROPICAL_FISH, listOf(.4)))
        registry.register(ModItems.MESH_FLINT, Fluids.WATER, Blocks.MYCELIUM, Lootable(Items.PUFFERFISH, listOf(.5)))

        registry.register(ModItems.MESH_IRON, Blocks.GRAVEL, Lootable(Items.DIAMOND, .01))
        registry.register(ModItems.MESH_DIAMOND, Blocks.GRAVEL, Lootable(Items.DIAMOND, .02))
    }

    override fun registerCrook(registry: IToolRegistry) {
        registry.registerTag(BlockTags.LEAVES, Lootable(Items.APPLE, 0.05))
        registry.registerTag(BlockTags.LEAVES, Lootable(Items.STICK, 0.01))
        registry.registerTag(BlockTags.LEAVES, Lootable(id("silkworm_raw"), listOf(0.1, 0.2, 0.2)))
        for(w in EnumVanillaWoodTypes.values()) {
            registry.registerDrops(w.getLeafBlock(), Lootable(w.getSeedItem(), 0.25))
        }
    }

    override fun registerHammer(registry: IToolRegistry) {
        // Stone
        registry.registerDrops(Blocks.STONE, Lootable(Blocks.COBBLESTONE, 1.0))
        registry.registerDrops(Blocks.COBBLESTONE, Lootable(Blocks.GRAVEL, 1.0))
        registry.registerDrops(Blocks.GRAVEL, Lootable(Blocks.SAND, 1.0))
        registry.registerTag(BlockTags.SAND, Lootable(id("silt"), 1.0))
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
        EnumVanillaColors.values().forEach { c ->
            // Concrete Hammering
            registry.registerDrops(c.getConcrete(), Lootable(c.getConcretePowder(), 1.0))
            registry.registerDrops(c.getConcretePowder(), Lootable(id("silt"), 1.0))
            registry.registerDrops(c.getConcretePowder(), Lootable(c.getDye(), 0.0625))
            // Wool Hammering
            registry.registerDrops(c.getWool(), Lootable(Items.STRING.asStack(4), 1.0))
            registry.registerDrops(c.getWool(), Lootable(c.getDye(), 0.5))
            // Glass Hammering
            registry.registerDrops(c.getGlass(), Lootable(Blocks.SAND, 1.0))
            registry.registerDrops(c.getGlass(), Lootable(c.getDye(), 0.0625))
        }

        //
    }

    override fun registerWitchWaterEntity(registry: IWitchWaterEntityRegistry) {

    }

    override fun registerWitchWaterFluid(registry: IWitchWaterFluidRegistry) {

    }

}