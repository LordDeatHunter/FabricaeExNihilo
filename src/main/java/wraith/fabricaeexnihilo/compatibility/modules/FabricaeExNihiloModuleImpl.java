package wraith.fabricaeexnihilo.compatibility.modules;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.api.compatibility.FabricaeExNihiloModule;
import wraith.fabricaeexnihilo.api.crafting.FluidIngredient;
import wraith.fabricaeexnihilo.api.crafting.ItemIngredient;
import wraith.fabricaeexnihilo.api.crafting.Lootable;
import wraith.fabricaeexnihilo.api.crafting.WeightedList;
import wraith.fabricaeexnihilo.api.recipes.barrel.AlchemyRecipe;
import wraith.fabricaeexnihilo.api.registry.*;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.barrels.modes.FluidMode;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
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

public final class FabricaeExNihiloModuleImpl implements FabricaeExNihiloModule {

    private FabricaeExNihiloModuleImpl() {}

    public static final FabricaeExNihiloModuleImpl INSTANCE = new FabricaeExNihiloModuleImpl();

    @Override
    public void registerAlchemy(AlchemyRecipeRegistry registry) {
        registry.register(Fluids.WATER, ItemUtils.getExNihiloItem("seed_mycelium"), WitchWaterFluid.STILL);

        registry.register(Fluids.LAVA, Items.GLOWSTONE_DUST, Blocks.END_STONE);
        registry.register(Fluids.LAVA, Items.REDSTONE, Blocks.NETHERRACK);
        registry.register(Fluids.WATER, ItemUtils.getExNihiloBlock("dust"), Blocks.CLAY);
        registry.register(Fluids.WATER, ItemUtils.getExNihiloBlock("silt"), Blocks.CLAY);

        registry.register(MilkFluid.TAG, Items.BROWN_MUSHROOM, Blocks.SLIME_BLOCK);
        registry.register(MilkFluid.TAG, Items.RED_MUSHROOM, Blocks.SLIME_BLOCK);

        registry.register(BloodFluid.TAG, Blocks.SAND, Blocks.RED_SAND);
        registry.register(BloodFluid.TAG, Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        registry.register(BloodFluid.TAG, Blocks.CHISELED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE);
        registry.register(BloodFluid.TAG, Blocks.CUT_SANDSTONE, Blocks.CUT_RED_SANDSTONE);
        registry.register(BloodFluid.TAG, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE);

        registry.register(AlchemyRecipe.builder()
                .withReactant(new FluidIngredient(Fluids.WATER))
                .withCatalyst(new ItemIngredient(ItemUtils.getExNihiloItem("salt_bottle")))
                .withProduct(new FluidMode(FluidKeys.get(BrineFluid.STILL).withAmount(FluidAmount.BUCKET)))
                .withByproduct(new Lootable(ItemUtils.asStack(Items.GLASS_BOTTLE), 1.0))
                .build());

        registry.register(AlchemyRecipe.builder()
                .withReactant(new FluidIngredient(Fluids.WATER))
                .withCatalyst(new ItemIngredient(TagFactory.ITEM.create(new Identifier("c:salt"))))
                .withProduct(new FluidMode(FluidKeys.get(BrineFluid.STILL).withAmount(FluidAmount.BUCKET)))
                .build());

        registry.register(BrineFluid.TAG, Items.BLUE_DYE, Blocks.TUBE_CORAL_BLOCK);
        registry.register(BrineFluid.TAG, Items.PINK_DYE, Blocks.BRAIN_CORAL_BLOCK);
        registry.register(BrineFluid.TAG, Items.MAGENTA_DYE, Blocks.BUBBLE_CORAL_BLOCK);
        registry.register(BrineFluid.TAG, Items.RED_DYE, Blocks.FIRE_CORAL_BLOCK);
        registry.register(BrineFluid.TAG, Items.YELLOW_DYE, Blocks.HORN_CORAL_BLOCK);

        registry.register(new FluidIngredient(BrineFluid.STILL), new ItemIngredient(ItemTags.WOOL), ItemUtils.asStack(Blocks.WET_SPONGE));

        registry.register(FluidTags.LAVA, ItemUtils.getExNihiloItemStack("doll_blaze"), EntityType.BLAZE);
        registry.register(new FluidIngredient(WitchWaterFluid.FLOWING, WitchWaterFluid.STILL), ItemUtils.getExNihiloItemStack("doll_enderman"), EntityType.ENDERMAN);
        registry.register(BrineFluid.TAG, ItemUtils.getExNihiloItemStack("doll_guardian"), EntityType.GUARDIAN);
        registry.register(new FluidIngredient(WitchWaterFluid.FLOWING, WitchWaterFluid.STILL), ItemUtils.getExNihiloItemStack("doll_shulker"), EntityType.SHULKER);
    }

    @Override
    public void registerCompost(CompostRecipeRegistry registry) {
        registry.register(ItemTags.LEAVES, Blocks.DIRT, 0.125, Color.DARK_GREEN);
        registry.register(ItemTags.SAPLINGS, Blocks.DIRT, 0.0625, Color.DARK_GREEN);

        registry.register(Items.CHORUS_FLOWER, Blocks.END_STONE, 0.25, Color.DARK_PURPLE);
        registry.register(Items.POPPED_CHORUS_FRUIT, Blocks.END_STONE, 0.125, Color.LIGHT_PURPLE);
        registry.register(Items.CHORUS_FRUIT, Blocks.END_STONE, 0.0625, Color.LIGHT_PURPLE);

        registry.register(Items.COBWEB, Blocks.WHITE_WOOL, 0.5, Color.WHITE);

        registry.register(Items.CACTUS, Blocks.DIRT, 0.0625, Color.DARK_GREEN);

        registry.register(TagFactory.ITEM.create(new Identifier("c:seeds")), Blocks.DIRT, 0.0625, Color.GREEN);
        registry.register(TagFactory.ITEM.create(new Identifier("c:veggies")), Blocks.DIRT, 0.0625, Color.YELLOW);
        registry.register(ItemTags.SMALL_FLOWERS, Blocks.DIRT, 0.0625, Color.RED);
        registry.register(TagFactory.ITEM.create(new Identifier("c:dyes")), Blocks.DIRT, 0.125, Color.RED);
        registry.register(TagFactory.ITEM.create(new Identifier("c:raw_meat")), Blocks.DIRT, 0.125, Color.RED);
        registry.register(TagFactory.ITEM.create(new Identifier("c:cooked_meat")), Blocks.DIRT, 0.25, Color.RED);
    }

    @Override
    public void registerLeaking(LeakingRecipeRegistry registry) {
        registry.register(Blocks.COBBLESTONE, Fluids.WATER, FluidAmount.BUCKET.as1620() / 10, Blocks.MOSSY_COBBLESTONE);
        registry.register(Blocks.STONE_BRICKS, Fluids.WATER, FluidAmount.BUCKET.as1620() / 10, Blocks.MOSSY_STONE_BRICKS);

        registry.register(ItemTags.SAPLINGS, WitchWaterFluid.STILL, FluidAmount.BUCKET.as1620() / 10, Blocks.DEAD_BUSH);
        registry.register(Blocks.GRAVEL, WitchWaterFluid.STILL, FluidAmount.BUCKET.as1620() / 10, ItemUtils.getExNihiloBlock("crushed_netherrack"));
        registry.register(Blocks.SAND, WitchWaterFluid.STILL, FluidAmount.BUCKET.as1620() / 2, Blocks.SOUL_SAND);
        registry.register(Blocks.PODZOL, WitchWaterFluid.STILL, FluidAmount.BUCKET.as1620() / 2, Blocks.MYCELIUM);
        registry.register(ItemTags.SMALL_FLOWERS, WitchWaterFluid.STILL, FluidAmount.BUCKET.as1620() / 2, Blocks.BROWN_MUSHROOM);

        registry.register(Blocks.COBBLESTONE, BloodFluid.TAG, FluidAmount.BUCKET.as1620() / 10, Blocks.NETHERRACK);
        registry.register(Blocks.GRAVEL, BloodFluid.TAG, FluidAmount.BUCKET.as1620() / 20, ItemUtils.getExNihiloBlock("crushed_netherrack"));
    }

    @Override
    public void registerMilking(MilkingRecipeRegistry registry) {
        registry.register(EntityType.COW, MilkFluid.STILL, FluidAmount.BUCKET.as1620() / 100, 20);
        registry.register(EntityType.WITCH, WitchWaterFluid.STILL, FluidAmount.BUCKET.as1620() / 100, 20);
    }

    @Override
    public void registerCrucibleHeat(CrucibleHeatRecipeRegistry registry) {
        registry.register(Blocks.TORCH, 1);
        registry.register(FluidTags.LAVA, 4);
        registry.register(Blocks.MAGMA_BLOCK, 3);
        registry.register(Blocks.GLOWSTONE, 2);
        registry.register(Blocks.FIRE, 5);
    }

    @Override
    public void registerCrucibleStone(CrucibleRecipeRegistry registry) {
        registry.register(Blocks.NETHERRACK, Fluids.LAVA,FluidAmount.BUCKET.as1620() / 2);
        registry.register(Blocks.COBBLESTONE, Fluids.LAVA,FluidAmount.BUCKET.as1620() / 4);
        registry.register(Blocks.GRAVEL, Fluids.LAVA,FluidAmount.BUCKET.as1620() / 8);
        registry.register(ItemTags.SAND, Fluids.LAVA,FluidAmount.BUCKET.as1620() / 16);
    }

    @Override
    public void registerCrucibleWood(CrucibleRecipeRegistry registry) {
        registry.register(ItemTags.SAPLINGS, Fluids.WATER, FluidAmount.BUCKET.as1620() / 10);
        registry.register(ItemTags.LEAVES, Fluids.WATER, FluidAmount.BUCKET.as1620() / 4);
        registry.register(ItemTags.SMALL_FLOWERS, Fluids.WATER, FluidAmount.BUCKET.as1620() / 10);
        registry.register(TagFactory.ITEM.create(new Identifier("c:seeds")), Fluids.WATER, FluidAmount.BUCKET.as1620() / 10);
        registry.register(TagFactory.ITEM.create(new Identifier("c:veggies")), Fluids.WATER, FluidAmount.BUCKET.as1620() / 10);
    }

    @Override
    public void registerOres(OreRecipeRegistry registry) {
        // TODO("Implement tag checking to prevent creation of unnecessary ores")
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

        registry.register(flintMesh, dust, new Lootable(Items.REDSTONE, 0.1));
        registry.register(ironMesh, dust, new Lootable(Items.REDSTONE, 0.2));
        registry.register(goldMesh, dust, new Lootable(Items.REDSTONE, 0.3));
        registry.register(diamondMesh, dust, new Lootable(Items.REDSTONE, 0.5, .5));

        registry.register(flintMesh, silt, new Lootable(Items.LAPIS_LAZULI, 0.02));
        registry.register(ironMesh, silt, new Lootable(Items.LAPIS_LAZULI, 0.05));
        registry.register(goldMesh, silt, new Lootable(Items.LAPIS_LAZULI, 0.5, 0.5, 0.1));
        registry.register(diamondMesh, silt, new Lootable(Items.LAPIS_LAZULI, 0.1));


        registry.register(stringMesh, Blocks.DIRT, new Lootable(ItemUtils.getExNihiloItem("pebble_stone"), 0.5));

        var rubber = ItemUtils.getExNihiloItemStack("seed_rubber");
        if(!rubber.isEmpty()) {
            registry.register(ironMesh, Blocks.DIRT, new Lootable(rubber, 0.02, 0.02, 0.01));
        }

        Stream.of("pebble_andesite", "pebble_diorite", "pebble_granite").map(ItemUtils::getExNihiloItem).forEach(item -> {
            registry.register(stringMesh, Blocks.DIRT, new Lootable(item, 0.05));
            registry.register(flintMesh, Blocks.DIRT, new Lootable(item, 0.2));
        });

        registry.register(stringMesh, Blocks.DIRT, new Lootable(Items.WHEAT_SEEDS, 0.05, 0.05, 0.05));
        registry.register(stringMesh, Blocks.DIRT, new Lootable(Items.BEETROOT_SEEDS, 0.05, 0.02, 0.02));
        registry.register(ironMesh, Blocks.DIRT, new Lootable(Items.SWEET_BERRIES, 0.02, 0.02, 0.01));

        registry.register(stringMesh, Blocks.DIRT, new Lootable(ItemUtils.getExNihiloItem("seed_carrot"), 0.05));
        registry.register(stringMesh, Blocks.DIRT, new Lootable(ItemUtils.getExNihiloItem("seed_grass"), 0.05));
        registry.register(ironMesh, Blocks.DIRT, new Lootable(ItemUtils.getExNihiloItem("seed_mycelium"), 0.02));
        registry.register(stringMesh, Blocks.DIRT, new Lootable(ItemUtils.getExNihiloItem("seed_potato"), 0.03));
        registry.register(flintMesh, ItemTags.SAND, new Lootable(ItemUtils.getExNihiloItem("seed_cactus"), 0.01));

        var chorusSeed = ItemUtils.getExNihiloItem("seed_chorus");
        registry.register(stringMesh, crushedEndstone, new Lootable(chorusSeed, 0.01));
        registry.register(flintMesh, crushedEndstone, new Lootable(chorusSeed, 0.02));
        registry.register(ironMesh, crushedEndstone, new Lootable(chorusSeed, 0.05));
        registry.register(goldMesh, crushedEndstone, new Lootable(chorusSeed, 0.2));
        registry.register(diamondMesh, crushedEndstone, new Lootable(chorusSeed, 0.1));

        ModItems.FLOWER_SEEDS.values().forEach(flowerSeed -> registry.register(flintMesh, Blocks.DIRT, new Lootable(flowerSeed, 0.05)));

        registry.register(flintMesh, Blocks.SOUL_SAND, new Lootable(Items.QUARTZ, 0.1));
        registry.register(ironMesh, Blocks.SOUL_SAND, new Lootable(Items.QUARTZ, 0.2));
        registry.register(goldMesh, Blocks.SOUL_SAND, new Lootable(Items.QUARTZ, 0.4));
        registry.register(diamondMesh, Blocks.SOUL_SAND, new Lootable(Items.QUARTZ, 0.3));

        registry.register(stringMesh, Blocks.DIRT, new Lootable(id("seed_oak"), 0.05));
        registry.register(stringMesh, Blocks.DIRT, new Lootable(id("seed_birch"), 0.02));
        registry.register(flintMesh, Blocks.DIRT, new Lootable(id("seed_spruce"), 0.05));
        registry.register(flintMesh, Blocks.DIRT, new Lootable(id("seed_jungle"), 0.01));
        registry.register(stringMesh, Blocks.RED_SAND, new Lootable(id("seed_acacia"), 0.02));
        registry.register(flintMesh, Blocks.DIRT, new Lootable(id("seed_dark_oak"), 0.01));

        registry.register(stringMesh, Blocks.GRAVEL, new Lootable(Items.FLINT, 0.5));
        registry.register(flintMesh, Blocks.GRAVEL, new Lootable(Items.FLINT, 0.3));

        registry.register(stringMesh, Fluids.WATER, Blocks.SAND, new Lootable(id("seed_sugarcane"), 0.1));
        registry.register(stringMesh, Fluids.WATER, Blocks.SAND, new Lootable(id("seed_kelp"), 0.05));

        registry.register(flintMesh, Fluids.WATER, Blocks.SAND, new Lootable(id("seed_sugarcane"), 0.15));
        registry.register(flintMesh, Fluids.WATER, Blocks.SAND, new Lootable(id("seed_kelp"), 0.1));

        registry.register(ironMesh, Fluids.WATER, Blocks.SAND, new Lootable(id("seed_sea_pickle"), 0.1));

        Arrays.asList(Items.TROPICAL_FISH, Items.COD, Items.SALMON).forEach(item -> {
            registry.register(stringMesh, Fluids.WATER, Blocks.DIRT, new Lootable(item, 0.05));
            registry.register(flintMesh, Fluids.WATER, Blocks.DIRT, new Lootable(item, 0.1));
            registry.register(ironMesh, Fluids.WATER, Blocks.DIRT, new Lootable(item, 0.15));
        });

        registry.register(ironMesh, Fluids.LAVA, Blocks.DIRT, new Lootable(Items.COOKED_COD, 0.15));
        registry.register(ironMesh, Fluids.LAVA, Blocks.DIRT, new Lootable(Items.COOKED_SALMON, 0.15));

        registry.register(stringMesh, Fluids.WATER, Blocks.MYCELIUM, new Lootable(Items.PUFFERFISH, 0.01));
        registry.register(flintMesh, Fluids.WATER, Blocks.MYCELIUM, new Lootable(Items.PUFFERFISH, 0.01));
        registry.register(goldMesh, Fluids.WATER, Blocks.MYCELIUM, new Lootable(Items.PUFFERFISH, 0.02));
        registry.register(diamondMesh, Fluids.WATER, Blocks.MYCELIUM, new Lootable(Items.PUFFERFISH, 0.05));

        registry.register(stringMesh, Fluids.WATER, Blocks.GRAVEL, new Lootable(Items.PRISMARINE_SHARD, 0.01));
        registry.register(flintMesh, Fluids.WATER, Blocks.GRAVEL, new Lootable(Items.PRISMARINE_SHARD, 0.02));
        registry.register(ironMesh, Fluids.WATER, Blocks.GRAVEL, new Lootable(Items.PRISMARINE_SHARD, 0.05));
        registry.register(goldMesh, Fluids.WATER, Blocks.GRAVEL, new Lootable(Items.PRISMARINE_SHARD, 0.2));
        registry.register(diamondMesh, Fluids.WATER, Blocks.GRAVEL, new Lootable(Items.PRISMARINE_SHARD, 0.1));

        registry.register(stringMesh, Fluids.WATER, silt, new Lootable(Items.LILY_PAD, 0.1));
        registry.register(flintMesh, Fluids.WATER, silt, new Lootable(Items.LILY_PAD, 0.2));
        registry.register(ironMesh, Fluids.WATER, silt, new Lootable(Items.LILY_PAD, 0.4));

        registry.register(stringMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(id("seed_mycelium"), 0.01));
        registry.register(flintMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(id("seed_mycelium"), 0.02));
        registry.register(ironMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(id("seed_mycelium"), 0.05));
        registry.register(goldMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(id("seed_mycelium"), 0.01));
        registry.register(diamondMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(id("seed_mycelium"), 0.2));

        Arrays.asList(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM).forEach(item -> {
            registry.register(stringMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(item, 0.01));
            registry.register(flintMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(item, 0.02));
            registry.register(ironMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(item, 0.05));
            registry.register(goldMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(item, 0.1));
            registry.register(diamondMesh, WitchWaterFluid.STILL, Blocks.DIRT, new Lootable(item, 0.2));
        });

        registry.register(stringMesh, Blocks.GRAVEL, new Lootable(Items.FLINT, 0.6));
        registry.register(flintMesh, Blocks.GRAVEL, new Lootable(Items.FLINT, 0.4));

        registry.register(ironMesh, Blocks.GRAVEL, new Lootable(Items.DIAMOND, 0.01));
        registry.register(goldMesh, Blocks.GRAVEL, new Lootable(Items.DIAMOND, 0.02));
        registry.register(diamondMesh, Blocks.GRAVEL, new Lootable(Items.DIAMOND, 0.03));

        registry.register(flintMesh, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 0.2));
        registry.register(ironMesh, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 0.3));
        registry.register(goldMesh, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 0.4));
        registry.register(diamondMesh, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 0.5));

        registry.register(flintMesh, Fluids.WATER, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.2));
        registry.register(ironMesh, Fluids.WATER, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.3));
        registry.register(goldMesh, Fluids.WATER, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.4));
        registry.register(diamondMesh, Fluids.WATER, crushedPrismarine, new Lootable(Items.PRISMARINE_CRYSTALS, 1.0, 0.5));

        for(var ore : FabricaeExNihiloRegistries.ORES.getAll()) {

            if(!Registry.ITEM.containsId(ore.getPieceID())) {
                continue;
            }

            switch(ore.getMaterial()) {
                    case "iron" -> {
                registry.register(stringMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.01));
                registry.register(flintMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.05));
                registry.register(ironMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.15));
                registry.register(goldMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.20));
                registry.register(diamondMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.15));

                registry.register(stringMesh, Blocks.RED_SAND, new Lootable(ore.getPieceID(), 0.05));
                registry.register(flintMesh, Blocks.RED_SAND, new Lootable(ore.getPieceID(), 0.1));
                registry.register(ironMesh, Blocks.RED_SAND, new Lootable(ore.getPieceID(), 0.2));
                registry.register(goldMesh, Blocks.RED_SAND, new Lootable(ore.getPieceID(), 0.5, 0.5));
                registry.register(diamondMesh, Blocks.RED_SAND, new Lootable(ore.getPieceID(), 1.0));

                registry.register(stringMesh, crushedGranite, new Lootable(ore.getPieceID(), 0.02));
                registry.register(flintMesh, crushedGranite, new Lootable(ore.getPieceID(), 0.05));
                registry.register(ironMesh, crushedGranite, new Lootable(ore.getPieceID(), 0.1, 0.1));
                registry.register(goldMesh, crushedGranite, new Lootable(ore.getPieceID(), 0.6, 0.6));
                registry.register(diamondMesh, crushedGranite, new Lootable(ore.getPieceID(), 01.0, 0.5));
            }
                case "gold" -> {
                registry.register(ironMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.05));
                registry.register(goldMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.075));
                registry.register(diamondMesh, Blocks.GRAVEL, new Lootable(ore.getPieceID(), 0.1));

                registry.register(ironMesh, crushedNetherrack, new Lootable(ore.getPieceID(), 0.1, 0.1));
                registry.register(goldMesh, crushedNetherrack, new Lootable(ore.getPieceID(), 0.15, 0.15));
                registry.register(diamondMesh, crushedNetherrack, new Lootable(ore.getPieceID(), 0.2, 0.2));
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
                registry.register(ironMesh, sieveable, new Lootable(ore.getPieceID(), 0.05));
                registry.register(goldMesh, sieveable, new Lootable(ore.getPieceID(), 0.075));
                registry.register(diamondMesh, sieveable, new Lootable(ore.getPieceID(), 0.1));
            }
            }
        }

        registry.register(stringMesh, Fluids.LAVA,crushedGranite, new Lootable(Items.IRON_NUGGET, 0.1));
        registry.register(flintMesh, Fluids.LAVA, crushedGranite, new Lootable(Items.IRON_NUGGET, 0.2));
        registry.register(ironMesh, Fluids.LAVA, crushedGranite, new Lootable(Items.IRON_NUGGET, 0.1, 0.1));
        registry.register(goldMesh, Fluids.LAVA, crushedGranite, new Lootable(Items.IRON_NUGGET, 0.2, 0.2));
        registry.register(diamondMesh, Fluids.LAVA, crushedGranite, new Lootable(Items.IRON_NUGGET, 0.3, 0.3));
    }

    @Override
    public void registerCrook(ToolRecipeRegistry registry) {
        registry.register(ItemTags.LEAVES, new Lootable(Items.STICK, 0.01));
        registry.register(ItemTags.LEAVES, new Lootable(id("silkworm_raw"), 0.1, 0.2, 0.2));
        if (ModTags.INFESTED_LEAVES != null) {
            registry.register(ModTags.INFESTED_LEAVES, new Lootable(ItemUtils.asStack(Items.STRING, 1), 1.0, 1.0, 0.5, 0.2, 0.1));
        }
        for(var wood : VanillaWoodDefinitions.values()) {
            registry.register(wood.getLeafBlock(), new Lootable(wood.getSeedItem(), 0.25));
        }
    }

    @Override
    public void registerHammer(ToolRecipeRegistry registry) {
        // Stone
        registry.register(Blocks.STONE, new Lootable(Blocks.COBBLESTONE, 1.0));
        registry.register(Blocks.COBBLESTONE, new Lootable(Blocks.GRAVEL, 1.0, 0.25));
        registry.register(Blocks.GRAVEL, new Lootable(Blocks.SAND, 1.0, 0.25));
        registry.register(Blocks.SAND, new Lootable(id("silt"), 1.0, 0.25));
        registry.register(id("silt"), new Lootable(id("dust"), 1.0, 0.25));

        // Andesite
        registry.register(Blocks.ANDESITE, new Lootable(id("crushed_andesite"), 1.0, 0.5));
        registry.register(id("crushed_andesite"), new Lootable(Blocks.LIGHT_GRAY_CONCRETE_POWDER, 1.0, 0.5));

        // Diorite
        registry.register(Blocks.DIORITE, new Lootable(id("crushed_diorite"), 1.0, 0.5));
        registry.register(id("crushed_diorite"), new Lootable(Items.WHITE_CONCRETE_POWDER, 1.0, 0.5));

        // Granite
        registry.register(Blocks.GRANITE, new Lootable(id("crushed_granite"), 1.0, 0.5));
        registry.register(id("crushed_granite"), new Lootable(Items.RED_SAND, 1.0, 0.5));

        // Netherrack
        registry.register(Blocks.NETHERRACK, new Lootable(id("crushed_netherrack"), 1.0, 0.5));
        registry.register(Blocks.NETHER_BRICKS, new Lootable(id("crushed_netherrack"), 1.0, 0.5));
        registry.register(id("crushed_netherrack"), new Lootable(Blocks.RED_CONCRETE_POWDER, 1.0, 0.5));

        // End Stone
        registry.register(Blocks.END_STONE, new Lootable(id("crushed_endstone"), 1.0));
        registry.register(Blocks.END_STONE_BRICKS, new Lootable(id("crushed_endstone"), 1.0, 0.5));
        registry.register(id("crushed_endstone"), new Lootable(Blocks.YELLOW_CONCRETE_POWDER, 1.0, 0.5));

        // Prismarine
        registry.register(Blocks.PRISMARINE, new Lootable(id("crushed_prismarine"), 1.0));
        registry.register(id("crushed_prismarine"), new Lootable(Blocks.CYAN_CONCRETE_POWDER, 1.0, 0.5));

        // Misc.
        registry.register(ItemTags.WOOL, new Lootable(ItemUtils.asStack(Items.STRING, 4), 1.0));
        Arrays.stream(DyeColor.values()).forEach(color -> {
            // Concrete Hammering
            registry.register(getConcrete(color), new Lootable(getConcretePowder(color), 1.0));
            registry.register(getConcretePowder(color), new Lootable(id("silt"), 1.0));
            registry.register(getConcretePowder(color), new Lootable(getDye(color), 0.0625));
            // Wool Hammering
            registry.register(getWool(color), new Lootable(getDye(color), 0.5));
            // Glass Hammering
            registry.register(getGlass(color), new Lootable(Blocks.SAND, 1.0));
            registry.register(getGlass(color), new Lootable(getDye(color), 0.0625));
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

    @Override
    public void registerWitchWaterWorld(WitchWaterWorldRecipeRegistry registry) {
        registry.register(new FluidIngredient(Fluids.WATER, Fluids.FLOWING_WATER), new WeightedList(
                new Pair<>(Blocks.DIRT, 51),
                new Pair<>(Blocks.GRASS_BLOCK, 12),
                new Pair<>(Blocks.COARSE_DIRT, 12),
                new Pair<>(Blocks.MYCELIUM, 12),
                new Pair<>(Blocks.PODZOL, 12)
        ));
        registry.register(new FluidIngredient(FluidTags.LAVA), new WeightedList(
                new Pair<>(Blocks.COBBLESTONE, 3),
                new Pair<>(Blocks.ANDESITE, 1),
                new Pair<>(Blocks.DIORITE, 1),
                new Pair<>(Blocks.GRANITE, 1)
        ));
        registry.register(new FluidIngredient(BrineFluid.TAG), new WeightedList(
                new Pair<>(Blocks.DEAD_BRAIN_CORAL_BLOCK, 16),
                new Pair<>(Blocks.DEAD_BUBBLE_CORAL_BLOCK, 16),
                new Pair<>(Blocks.DEAD_FIRE_CORAL_BLOCK, 16),
                new Pair<>(Blocks.DEAD_HORN_CORAL_BLOCK, 16),
                new Pair<>(Blocks.DEAD_TUBE_CORAL_BLOCK, 16),
                new Pair<>(Blocks.BRAIN_CORAL_BLOCK, 4),
                new Pair<>(Blocks.BUBBLE_CORAL_BLOCK, 4),
                new Pair<>(Blocks.FIRE_CORAL_BLOCK, 4),
                new Pair<>(Blocks.HORN_CORAL_BLOCK, 4),
                new Pair<>(Blocks.TUBE_CORAL_BLOCK, 4),
                new Pair<>(Blocks.PRISMARINE, 1)
        ));
        registry.register(new FluidIngredient(TagFactory.FLUID.create(new Identifier("c:blood"))), new WeightedList(
                new Pair<>(Blocks.NETHERRACK, 1),
                new Pair<>(Blocks.RED_SAND, 16),
                new Pair<>(Blocks.FIRE_CORAL_BLOCK, 4)
        ));
    }

    @Override
    public void registerWitchWaterEntity(WitchWaterEntityRecipeRegistry registry) {
        registry.register(EntityType.SKELETON, EntityType.WITHER_SKELETON);
        registry.register(EntityType.SLIME, EntityType.MAGMA_CUBE);
        registry.register(EntityType.SPIDER, EntityType.CAVE_SPIDER);

        registry.register(EntityType.COW, EntityType.MOOSHROOM);
        registry.register(EntityType.PIG, EntityType.PIGLIN);
        registry.register(EntityType.CHICKEN, EntityType.VEX);
        registry.register(EntityType.SQUID, EntityType.GHAST);
        registry.register(EntityType.PANDA, EntityType.RAVAGER);
        registry.register(EntityType.POLAR_BEAR, EntityType.RAVAGER);
        registry.register(EntityType.HORSE, EntityType.SKELETON_HORSE);
        registry.register(EntityType.DONKEY, EntityType.ZOMBIE_HORSE);
        registry.register(EntityType.MULE, EntityType.ZOMBIE_HORSE);
        registry.register(EntityType.BAT, EntityType.PHANTOM);
        registry.register(EntityType.PARROT, EntityType.PHANTOM);
        registry.register(EntityType.TURTLE, EntityType.SHULKER);

        registry.register(EntityType.PUFFERFISH, EntityType.GUARDIAN);
        registry.register(EntityType.SALMON, EntityType.SILVERFISH);
        registry.register(EntityType.TROPICAL_FISH, EntityType.SILVERFISH);
        registry.register(EntityType.COD, EntityType.SILVERFISH);

        /*
         * Villagers
         */
        registry.register(EntityType.VILLAGER, VillagerProfession.ARMORER, EntityType.PILLAGER);
        registry.register(EntityType.VILLAGER, VillagerProfession.BUTCHER, EntityType.VINDICATOR);
        registry.register(EntityType.VILLAGER, VillagerProfession.CARTOGRAPHER, EntityType.PILLAGER);
        registry.register(EntityType.VILLAGER, VillagerProfession.CLERIC, EntityType.EVOKER);
        registry.register(EntityType.VILLAGER, VillagerProfession.FARMER, EntityType.HUSK);
        registry.register(EntityType.VILLAGER, VillagerProfession.FISHERMAN, EntityType.DROWNED);
        registry.register(EntityType.VILLAGER, VillagerProfession.FLETCHER, EntityType.STRAY);
        registry.register(EntityType.VILLAGER, VillagerProfession.LEATHERWORKER, EntityType.PILLAGER);
        registry.register(EntityType.VILLAGER, VillagerProfession.LIBRARIAN, EntityType.ILLUSIONER);
        registry.register(EntityType.VILLAGER, VillagerProfession.MASON, EntityType.PILLAGER);
        registry.register(EntityType.VILLAGER, VillagerProfession.NITWIT, EntityType.ZOMBIE_VILLAGER);
        registry.register(EntityType.VILLAGER, VillagerProfession.SHEPHERD, EntityType.PILLAGER);
        registry.register(EntityType.VILLAGER, VillagerProfession.TOOLSMITH, EntityType.PILLAGER);
        registry.register(EntityType.VILLAGER, VillagerProfession.WEAPONSMITH, EntityType.PILLAGER);
        // Do generic last.
        registry.register(EntityType.VILLAGER, VillagerProfession.NONE, EntityType.ZOMBIE_VILLAGER);

    }

    @Override
    public void registerFluidOnTop(FluidOnTopRecipeRegistry registry) {
        registry.register(Fluids.LAVA, Fluids.WATER, Blocks.OBSIDIAN);
        registry.register(Fluids.WATER, Fluids.LAVA, Blocks.STONE);
        registry.register(Fluids.WATER, BrineFluid.TAG, Blocks.ICE);
    }

    @Override
    public void registerFluidTransform(FluidTransformRecipeRegistry registry) {
        registry.register(Fluids.WATER, Blocks.MYCELIUM, WitchWaterFluid.STILL);
        registry.register(MilkFluid.STILL, Blocks.MYCELIUM, Blocks.SLIME_BLOCK);
    }
}
