package wraith.fabricaeexnihilo.compatibility.modules;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.api.compatibility.FabricaeExNihiloModule;
import wraith.fabricaeexnihilo.api.crafting.Loot;
import wraith.fabricaeexnihilo.api.registry.*;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.ore.ChunkMaterial;
import wraith.fabricaeexnihilo.modules.ore.ChunkShape;
import wraith.fabricaeexnihilo.modules.ore.PieceShape;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.ItemUtils;
import wraith.fabricaeexnihilo.util.VanillaWoodDefinitions;

import java.util.Arrays;
import java.util.stream.Stream;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;
import static wraith.fabricaeexnihilo.util.ColorUtils.*;

@SuppressWarnings("UnstableApiUsage")
public final class FabricaeExNihiloModuleImpl implements FabricaeExNihiloModule {

    private FabricaeExNihiloModuleImpl() {}

    public static final FabricaeExNihiloModuleImpl INSTANCE = new FabricaeExNihiloModuleImpl();
    
    @Override
    public void registerOres(OreRecipeRegistry registry) {
        // TODO: Implement tag checking to prevent creation of unnecessary ores
        // Vanilla Metals
        registry.register("iron", Color.IRON, PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.GRANITE);
        registry.register("gold", Color.GOLD, PieceShape.FINE, ChunkShape.CHUNK, ChunkMaterial.STONE);

        // Modded Metals
        registry.register("aluminum",  Color.ALUMINUM,  PieceShape.FINE,   ChunkShape.CHUNK, ChunkMaterial.SAND);
        registry.register("ardite",    Color.ARDITE,    PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.NETHERRACK);
        registry.register("beryllium", Color.BERYLLIUM, PieceShape.NORMAL, ChunkShape.FLINT, ChunkMaterial.STONE);
        registry.register("boron",     Color.BORON,     PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.SAND);
        registry.register("cobalt",    Color.COBALT,    PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.NETHERRACK);
        registry.register("copper",    Color.COPPER,    PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.STONE);
        registry.register("lead",      Color.LEAD,      PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.STONE);
        registry.register("lithium",   Color.LITHIUM,   PieceShape.FINE,   ChunkShape.FLINT, ChunkMaterial.SAND);
        registry.register("magnesium", Color.MAGNESIUM, PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.STONE);
        registry.register("nickel",    Color.NICKEL,    PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.STONE);
        registry.register("silver",    Color.SILVER,    PieceShape.NORMAL, ChunkShape.CHUNK, ChunkMaterial.STONE);
        registry.register("tin",       Color.TIN,       PieceShape.NORMAL, ChunkShape.LUMP, ChunkMaterial.DIORITE);
        registry.register("titanium",  Color.TITANIUM,  PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.STONE);
        registry.register("thorium",   Color.THORIUM,   PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.STONE);
        registry.register("tungsten",  Color.TUNGSTEN,  PieceShape.COARSE, ChunkShape.CHUNK, ChunkMaterial.NETHERRACK);
        registry.register("uranium",   Color.URANIUM,   PieceShape.COARSE, ChunkShape.LUMP, ChunkMaterial.STONE);
        registry.register("zinc",      Color.ZINC,      PieceShape.FINE,   ChunkShape.FLINT, ChunkMaterial.ANDESITE);
        registry.register("zirconium", Color.ZIRCONIUM, PieceShape.NORMAL, ChunkShape.FLINT, ChunkMaterial.ANDESITE);
    }

    @Override
    public void registerMesh(MeshRecipeRegistry registry) {
        registry.register(
                id("mesh_string"),
                ToolMaterials.WOOD.getEnchantability(),
                "item.minecraft.string",
                Color.WHITE,
                new Identifier("string")
        );
        registry.register(
                id("mesh_flint"),
                ToolMaterials.STONE.getEnchantability(),
                "item.minecraft.flint",
                Color.DARK_GRAY,
                new Identifier("flint")
        );
        registry.register(
                id("mesh_iron"),
                ToolMaterials.IRON.getEnchantability(),
                "Iron",
                new Color("777777"),
                new Identifier("iron_ingot")
        );
        registry.register(
                id("mesh_gold"),
                ToolMaterials.GOLD.getEnchantability(),
                "Gold",
                Color.GOLDEN,
                new Identifier("gold_ingot")
        );
        registry.register(
                id("mesh_diamond"),
                ToolMaterials.DIAMOND.getEnchantability(),
                "item.minecraft.diamond",
                Color.DARK_AQUA,
                new Identifier("diamond")
        );
    }

    @Override
    public void registerSieve(SieveRecipeRegistry registry) {
        var stringMesh = Registry.ITEM.get(id("mesh_string"));
        var flintMesh = Registry.ITEM.get(id("mesh_flint"));
        var ironMesh = Registry.ITEM.get(id("mesh_iron"));
        var goldMesh = Registry.ITEM.get(id("mesh_gold"));
        var diamondMesh = Registry.ITEM.get(id("mesh_diamond"));

        var crushedNetherrack = ItemUtils.getExNihiloBlock("crushed_netherrack");
        var crushedPrismarine = ItemUtils.getExNihiloBlock("crushed_prismarine");
        var crushedEndstone = ItemUtils.getExNihiloBlock("crushed_endstone");

        var crushedAndesite = ItemUtils.getExNihiloBlock("crushed_andesite");
        var crushedDiorite = ItemUtils.getExNihiloBlock("crushed_diorite");
        var crushedGranite = ItemUtils.getExNihiloBlock("crushed_granite");

        var dust = ItemUtils.getExNihiloBlock("dust");
        var silt = ItemUtils.getExNihiloBlock("silt");

        registry.register(flintMesh, dust, new Loot(Items.REDSTONE, 0.1));
        registry.register(ironMesh, dust, new Loot(Items.REDSTONE, 0.2));
        registry.register(goldMesh, dust, new Loot(Items.REDSTONE, 0.3));
        registry.register(diamondMesh, dust, new Loot(Items.REDSTONE, 0.5, 0.5));

        registry.register(flintMesh, silt, new Loot(Items.LAPIS_LAZULI, 0.02));
        registry.register(ironMesh, silt, new Loot(Items.LAPIS_LAZULI, 0.05));
        registry.register(goldMesh, silt, new Loot(Items.LAPIS_LAZULI, 0.5, 0.5, 0.1));
        registry.register(diamondMesh, silt, new Loot(Items.LAPIS_LAZULI, 0.1));


        registry.register(stringMesh, Blocks.DIRT, new Loot(ItemUtils.getExNihiloItem("pebble_stone"), 0.5));

        var rubber = ItemUtils.getExNihiloItemStack("seed_rubber");
        if(!rubber.isEmpty()) {
            registry.register(ironMesh, Blocks.DIRT, new Loot(rubber, 0.02, 0.02, 0.01));
        }

        Stream.of("pebble_andesite", "pebble_diorite", "pebble_granite").map(ItemUtils::getExNihiloItem).forEach(item -> {
            registry.register(stringMesh, Blocks.DIRT, new Loot(item, 0.05));
            registry.register(flintMesh, Blocks.DIRT, new Loot(item, 0.2));
        });

        registry.register(stringMesh, Blocks.DIRT, new Loot(Items.WHEAT_SEEDS, 0.05, 0.05, 0.05));
        registry.register(stringMesh, Blocks.DIRT, new Loot(Items.BEETROOT_SEEDS, 0.05, 0.02, 0.02));
        registry.register(ironMesh, Blocks.DIRT, new Loot(Items.SWEET_BERRIES, 0.02, 0.02, 0.01));

        registry.register(stringMesh, Blocks.DIRT, new Loot(ItemUtils.getExNihiloItem("seed_carrot"), 0.05));
        registry.register(stringMesh, Blocks.DIRT, new Loot(ItemUtils.getExNihiloItem("seed_grass"), 0.05));
        registry.register(ironMesh, Blocks.DIRT, new Loot(ItemUtils.getExNihiloItem("seed_mycelium"), 0.02));
        registry.register(stringMesh, Blocks.DIRT, new Loot(ItemUtils.getExNihiloItem("seed_potato"), 0.03));
        registry.register(flintMesh, ItemTags.SAND, new Loot(ItemUtils.getExNihiloItem("seed_cactus"), 0.01));

        var chorusSeed = ItemUtils.getExNihiloItem("seed_chorus");
        registry.register(stringMesh, crushedEndstone, new Loot(chorusSeed, 0.01));
        registry.register(flintMesh, crushedEndstone, new Loot(chorusSeed, 0.02));
        registry.register(ironMesh, crushedEndstone, new Loot(chorusSeed, 0.05));
        registry.register(goldMesh, crushedEndstone, new Loot(chorusSeed, 0.2));
        registry.register(diamondMesh, crushedEndstone, new Loot(chorusSeed, 0.1));

        ModItems.FLOWER_SEEDS.values().forEach(flowerSeed -> registry.register(flintMesh, Blocks.DIRT, new Loot(flowerSeed, 0.05)));

        registry.register(flintMesh, Blocks.SOUL_SAND, new Loot(Items.QUARTZ, 0.1));
        registry.register(ironMesh, Blocks.SOUL_SAND, new Loot(Items.QUARTZ, 0.2));
        registry.register(goldMesh, Blocks.SOUL_SAND, new Loot(Items.QUARTZ, 0.4));
        registry.register(diamondMesh, Blocks.SOUL_SAND, new Loot(Items.QUARTZ, 0.3));

        registry.register(stringMesh, Blocks.DIRT, new Loot(id("seed_oak"), 0.05));
        registry.register(stringMesh, Blocks.DIRT, new Loot(id("seed_birch"), 0.02));
        registry.register(flintMesh, Blocks.DIRT, new Loot(id("seed_spruce"), 0.05));
        registry.register(flintMesh, Blocks.DIRT, new Loot(id("seed_jungle"), 0.01));
        registry.register(stringMesh, Blocks.RED_SAND, new Loot(id("seed_acacia"), 0.02));
        registry.register(flintMesh, Blocks.DIRT, new Loot(id("seed_dark_oak"), 0.01));

        registry.register(stringMesh, Blocks.GRAVEL, new Loot(Items.FLINT, 0.5));
        registry.register(flintMesh, Blocks.GRAVEL, new Loot(Items.FLINT, 0.3));

        registry.register(stringMesh, Fluids.WATER, Blocks.SAND, new Loot(id("seed_sugarcane"), 0.1));
        registry.register(stringMesh, Fluids.WATER, Blocks.SAND, new Loot(id("seed_kelp"), 0.05));

        registry.register(flintMesh, Fluids.WATER, Blocks.SAND, new Loot(id("seed_sugarcane"), 0.15));
        registry.register(flintMesh, Fluids.WATER, Blocks.SAND, new Loot(id("seed_kelp"), 0.1));

        registry.register(ironMesh, Fluids.WATER, Blocks.SAND, new Loot(id("seed_sea_pickle"), 0.1));

        Arrays.asList(Items.TROPICAL_FISH, Items.COD, Items.SALMON).forEach(item -> {
            registry.register(stringMesh, Fluids.WATER, Blocks.DIRT, new Loot(item, 0.05));
            registry.register(flintMesh, Fluids.WATER, Blocks.DIRT, new Loot(item, 0.1));
            registry.register(ironMesh, Fluids.WATER, Blocks.DIRT, new Loot(item, 0.15));
        });

        registry.register(ironMesh, Fluids.LAVA, Blocks.DIRT, new Loot(Items.COOKED_COD, 0.15));
        registry.register(ironMesh, Fluids.LAVA, Blocks.DIRT, new Loot(Items.COOKED_SALMON, 0.15));

        registry.register(stringMesh, Fluids.WATER, Blocks.MYCELIUM, new Loot(Items.PUFFERFISH, 0.01));
        registry.register(flintMesh, Fluids.WATER, Blocks.MYCELIUM, new Loot(Items.PUFFERFISH, 0.01));
        registry.register(goldMesh, Fluids.WATER, Blocks.MYCELIUM, new Loot(Items.PUFFERFISH, 0.02));
        registry.register(diamondMesh, Fluids.WATER, Blocks.MYCELIUM, new Loot(Items.PUFFERFISH, 0.05));

        registry.register(stringMesh, Fluids.WATER, Blocks.GRAVEL, new Loot(Items.PRISMARINE_SHARD, 0.01));
        registry.register(flintMesh, Fluids.WATER, Blocks.GRAVEL, new Loot(Items.PRISMARINE_SHARD, 0.02));
        registry.register(ironMesh, Fluids.WATER, Blocks.GRAVEL, new Loot(Items.PRISMARINE_SHARD, 0.05));
        registry.register(goldMesh, Fluids.WATER, Blocks.GRAVEL, new Loot(Items.PRISMARINE_SHARD, 0.2));
        registry.register(diamondMesh, Fluids.WATER, Blocks.GRAVEL, new Loot(Items.PRISMARINE_SHARD, 0.1));

        registry.register(stringMesh, Fluids.WATER, silt, new Loot(Items.LILY_PAD, 0.1));
        registry.register(flintMesh, Fluids.WATER, silt, new Loot(Items.LILY_PAD, 0.2));
        registry.register(ironMesh, Fluids.WATER, silt, new Loot(Items.LILY_PAD, 0.4));

        registry.register(stringMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(id("seed_mycelium"), 0.01));
        registry.register(flintMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(id("seed_mycelium"), 0.02));
        registry.register(ironMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(id("seed_mycelium"), 0.05));
        registry.register(goldMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(id("seed_mycelium"), 0.01));
        registry.register(diamondMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(id("seed_mycelium"), 0.2));

        Arrays.asList(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM).forEach(item -> {
            registry.register(stringMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(item, 0.01));
            registry.register(flintMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(item, 0.02));
            registry.register(ironMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(item, 0.05));
            registry.register(goldMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(item, 0.1));
            registry.register(diamondMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Loot(item, 0.2));
        });

        registry.register(stringMesh, Blocks.GRAVEL, new Loot(Items.FLINT, 0.6));
        registry.register(flintMesh, Blocks.GRAVEL, new Loot(Items.FLINT, 0.4));

        registry.register(ironMesh, Blocks.GRAVEL, new Loot(Items.DIAMOND, 0.01));
        registry.register(goldMesh, Blocks.GRAVEL, new Loot(Items.DIAMOND, 0.02));
        registry.register(diamondMesh, Blocks.GRAVEL, new Loot(Items.DIAMOND, 0.03));

        registry.register(flintMesh, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 0.2));
        registry.register(ironMesh, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 0.3));
        registry.register(goldMesh, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 0.4));
        registry.register(diamondMesh, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 0.5));

        registry.register(flintMesh, Fluids.WATER, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 1.0, 0.2));
        registry.register(ironMesh, Fluids.WATER, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 1.0, 0.3));
        registry.register(goldMesh, Fluids.WATER, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 1.0, 0.4));
        registry.register(diamondMesh, Fluids.WATER, crushedPrismarine, new Loot(Items.PRISMARINE_CRYSTALS, 1.0, 0.5));

        for(var ore : FabricaeExNihiloRegistries.ORES.getAll()) {

            if(!Registry.ITEM.containsId(ore.getPieceID())) {
                continue;
            }

            switch(ore.getMaterial()) {
                    case "iron" -> {
                registry.register(stringMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.01));
                registry.register(flintMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.05));
                registry.register(ironMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.15));
                registry.register(goldMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.20));
                registry.register(diamondMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.15));

                registry.register(stringMesh, Blocks.RED_SAND, new Loot(ore.getPieceID(), 0.05));
                registry.register(flintMesh, Blocks.RED_SAND, new Loot(ore.getPieceID(), 0.1));
                registry.register(ironMesh, Blocks.RED_SAND, new Loot(ore.getPieceID(), 0.2));
                registry.register(goldMesh, Blocks.RED_SAND, new Loot(ore.getPieceID(), 0.5, 0.5));
                registry.register(diamondMesh, Blocks.RED_SAND, new Loot(ore.getPieceID(), 1.0));

                registry.register(stringMesh, crushedGranite, new Loot(ore.getPieceID(), 0.02));
                registry.register(flintMesh, crushedGranite, new Loot(ore.getPieceID(), 0.05));
                registry.register(ironMesh, crushedGranite, new Loot(ore.getPieceID(), 0.1, 0.1));
                registry.register(goldMesh, crushedGranite, new Loot(ore.getPieceID(), 0.6, 0.6));
                registry.register(diamondMesh, crushedGranite, new Loot(ore.getPieceID(), 01.0, 0.5));
            }
                case "gold" -> {
                registry.register(ironMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.05));
                registry.register(goldMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.075));
                registry.register(diamondMesh, Blocks.GRAVEL, new Loot(ore.getPieceID(), 0.1));

                registry.register(ironMesh, crushedNetherrack, new Loot(ore.getPieceID(), 0.1, 0.1));
                registry.register(goldMesh, crushedNetherrack, new Loot(ore.getPieceID(), 0.15, 0.15));
                registry.register(diamondMesh, crushedNetherrack, new Loot(ore.getPieceID(), 0.2, 0.2));
            }
                default -> {
                var sieveable = switch (ore.getChunkMaterial()) {
                    case ANDESITE -> crushedAndesite;
                    case DIORITE -> crushedDiorite;
                    case ENDSTONE -> crushedEndstone;
                    case GRANITE -> crushedGranite;
                    case NETHERRACK -> crushedNetherrack;
                    case PRISMARINE -> crushedPrismarine;
                    case REDSAND -> Blocks.RED_SAND;
                    case SAND -> Blocks.SAND;
                    case SOULSAND -> Blocks.SOUL_SAND;
                    case STONE -> Blocks.GRAVEL;
                };
                registry.register(ironMesh, sieveable, new Loot(ore.getPieceID(), 0.05));
                registry.register(goldMesh, sieveable, new Loot(ore.getPieceID(), 0.075));
                registry.register(diamondMesh, sieveable, new Loot(ore.getPieceID(), 0.1));
            }
            }
        }

        registry.register(stringMesh, Fluids.LAVA,crushedGranite, new Loot(Items.IRON_NUGGET, 0.1));
        registry.register(flintMesh, Fluids.LAVA, crushedGranite, new Loot(Items.IRON_NUGGET, 0.2));
        registry.register(ironMesh, Fluids.LAVA, crushedGranite, new Loot(Items.IRON_NUGGET, 0.1, 0.1));
        registry.register(goldMesh, Fluids.LAVA, crushedGranite, new Loot(Items.IRON_NUGGET, 0.2, 0.2));
        registry.register(diamondMesh, Fluids.LAVA, crushedGranite, new Loot(Items.IRON_NUGGET, 0.3, 0.3));
    }

    @Override
    public void registerCrook(ToolRecipeRegistry registry) {
        registry.register(ItemTags.LEAVES, new Loot(Items.STICK, 0.01));
        registry.register(ItemTags.LEAVES, new Loot(id("silkworm_raw"), 0.1, 0.2, 0.2));
        if (ModTags.INFESTED_LEAVES != null) {
            registry.register(ModTags.INFESTED_LEAVES, new Loot(ItemUtils.asStack(Items.STRING, 1), 1.0, 1.0, 0.5, 0.2, 0.1));
        }
        for(var wood : VanillaWoodDefinitions.values()) {
            registry.register(wood.getLeafBlock(), new Loot(wood.getSeedItem(), 0.25));
        }
    }

    @Override
    public void registerHammer(ToolRecipeRegistry registry) {
        // Stone
        registry.register(Blocks.STONE, new Loot(Blocks.COBBLESTONE, 1.0));
        registry.register(Blocks.COBBLESTONE, new Loot(Blocks.GRAVEL, 1.0, 0.25));
        registry.register(Blocks.GRAVEL, new Loot(Blocks.SAND, 1.0, 0.25));
        registry.register(Blocks.SAND, new Loot(id("silt"), 1.0, 0.25));
        registry.register(id("silt"), new Loot(id("dust"), 1.0, 0.25));

        // Andesite
        registry.register(Blocks.ANDESITE, new Loot(id("crushed_andesite"), 1.0, 0.5));
        registry.register(id("crushed_andesite"), new Loot(Blocks.LIGHT_GRAY_CONCRETE_POWDER, 1.0, 0.5));

        // Diorite
        registry.register(Blocks.DIORITE, new Loot(id("crushed_diorite"), 1.0, 0.5));
        registry.register(id("crushed_diorite"), new Loot(Items.WHITE_CONCRETE_POWDER, 1.0, 0.5));

        // Granite
        registry.register(Blocks.GRANITE, new Loot(id("crushed_granite"), 1.0, 0.5));
        registry.register(id("crushed_granite"), new Loot(Items.RED_SAND, 1.0, 0.5));

        // Netherrack
        registry.register(Blocks.NETHERRACK, new Loot(id("crushed_netherrack"), 1.0, 0.5));
        registry.register(Blocks.NETHER_BRICKS, new Loot(id("crushed_netherrack"), 1.0, 0.5));
        registry.register(id("crushed_netherrack"), new Loot(Blocks.RED_CONCRETE_POWDER, 1.0, 0.5));

        // End Stone
        registry.register(Blocks.END_STONE, new Loot(id("crushed_endstone"), 1.0));
        registry.register(Blocks.END_STONE_BRICKS, new Loot(id("crushed_endstone"), 1.0, 0.5));
        registry.register(id("crushed_endstone"), new Loot(Blocks.YELLOW_CONCRETE_POWDER, 1.0, 0.5));

        // Prismarine
        registry.register(Blocks.PRISMARINE, new Loot(id("crushed_prismarine"), 1.0));
        registry.register(id("crushed_prismarine"), new Loot(Blocks.CYAN_CONCRETE_POWDER, 1.0, 0.5));

        // Misc.
        registry.register(ItemTags.WOOL, new Loot(ItemUtils.asStack(Items.STRING, 4), 1.0));
        Arrays.stream(DyeColor.values()).forEach(color -> {
            // Concrete Hammering
            registry.register(getConcrete(color), new Loot(getConcretePowder(color), 1.0));
            registry.register(getConcretePowder(color), new Loot(id("silt"), 1.0));
            registry.register(getConcretePowder(color), new Loot(getDye(color), 0.0625));
            // Wool Hammering
            registry.register(getWool(color), new Loot(getDye(color), 0.5));
            // Glass Hammering
            registry.register(getGlass(color), new Loot(Blocks.SAND, 1.0));
            registry.register(getGlass(color), new Loot(getDye(color), 0.0625));
        });

        // Corals
        registry.register(Blocks.TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL, 1.0, 1.0, 0.5, 0.1);
        registry.register(Blocks.BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL, 1.0, 1.0, 0.5, 0.1);
        registry.register(Blocks.BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL, 1.0, 1.0, 0.5, 0.1);
        registry.register(Blocks.FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL, 1.0, 1.0, 0.5, 0.1);
        registry.register(Blocks.HORN_CORAL_BLOCK, Blocks.HORN_CORAL, 1.0, 1.0, 0.5, 0.1);

        registry.register(Blocks.TUBE_CORAL, Blocks.TUBE_CORAL_FAN, 1.0, 0.5);
        registry.register(Blocks.BRAIN_CORAL, Blocks.BRAIN_CORAL_FAN, 1.0, 0.5);
        registry.register(Blocks.BUBBLE_CORAL, Blocks.BUBBLE_CORAL_FAN, 1.0, 0.5);
        registry.register(Blocks.FIRE_CORAL, Blocks.FIRE_CORAL_FAN, 1.0, 0.5);
        registry.register(Blocks.HORN_CORAL, Blocks.HORN_CORAL_FAN, 1.0, 0.5);
    }
    
}
