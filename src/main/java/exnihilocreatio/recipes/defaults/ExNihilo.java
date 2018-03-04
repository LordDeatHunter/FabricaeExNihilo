package exnihilocreatio.recipes.defaults;

import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.ModBlocks;
import exnihilocreatio.ModFluids;
import exnihilocreatio.ModItems;
import exnihilocreatio.blocks.BlockSieve.MeshType;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.EnumPebbleSubtype;
import exnihilocreatio.items.ItemResource;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.items.seeds.ItemSeedBase;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.*;
import exnihilocreatio.registries.types.Meltable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import exnihilocreatio.util.Util;
import lombok.Getter;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ExNihilo implements IRecipeDefaults {
    @Getter
    public String MODID = ExNihiloCreatio.MODID;


    public void registerCompost(CompostRegistry registry) {
        IBlockState dirtState = Blocks.DIRT.getDefaultState();

        registry.register("treeSapling", 0.125f, dirtState);
        registry.register("treeLeaves", 0.125f, dirtState);
        registry.register("flower", 0.1f, dirtState);
        registry.register("fish", 0.15f, dirtState);
        registry.register("listAllmeatcooked", 0.20f, dirtState);

        registry.register(new ItemInfo(Items.ROTTEN_FLESH, 0), 0.1f, dirtState, new Color("C45631"));

        registry.register(new ItemInfo(Items.SPIDER_EYE, 0), 0.08f, dirtState, new Color("963E44"));

        registry.register(new ItemInfo(Items.WHEAT, 0), 0.08f, dirtState, new Color("E3E162"));
        registry.register(new ItemInfo(Items.WHEAT_SEEDS, 0), 0.08f, dirtState, new Color("35A82A"));
        registry.register(new ItemInfo(Items.BREAD, 0), 0.16f, dirtState, new Color("D1AF60"));

        registry.register(new BlockInfo(Blocks.BROWN_MUSHROOM, 0), 0.10f, dirtState, new Color("CFBFB6"));
        registry.register(new BlockInfo(Blocks.RED_MUSHROOM, 0), 0.10f, dirtState, new Color("D6A8A5"));

        registry.register(new ItemInfo(Items.PUMPKIN_PIE, 0), 0.16f, dirtState, new Color("E39A6D"));

        registry.register(new ItemInfo(Items.PORKCHOP, 0), 0.2f, dirtState, new Color("FFA091"));
        registry.register(new ItemInfo(Items.BEEF, 0), 0.2f, dirtState, new Color("FF4242"));
        registry.register(new ItemInfo(Items.CHICKEN, 0), 0.2f, dirtState, new Color("FFE8E8"));

        registry.register(new ItemInfo(ModItems.resources, ItemResource.getMetaFromName(ItemResource.SILKWORM)), 0.04f, dirtState, new Color("ff9966"));
        registry.register(new ItemInfo(ModItems.cookedSilkworm, 0), 0.04f, dirtState, new Color("cc6600"));

        registry.register(new ItemInfo(Items.APPLE, 0), 0.10f, dirtState, new Color("FFF68F"));
        registry.register(new ItemInfo(Items.MELON, 0), 0.04f, dirtState, new Color("FF443B"));
        registry.register(new BlockInfo(Blocks.MELON_BLOCK, 0), 1.0f / 6, dirtState, new Color("FF443B"));
        registry.register(new BlockInfo(Blocks.PUMPKIN, 0), 1.0f / 6, dirtState, new Color("FFDB66"));
        registry.register(new BlockInfo(Blocks.LIT_PUMPKIN, 0), 1.0f / 6, dirtState, new Color("FFDB66"));

        registry.register(new BlockInfo(Blocks.CACTUS, 0), 0.10f, dirtState, new Color("DEFFB5"));

        registry.register(new ItemInfo(Items.CARROT, 0), 0.08f, dirtState, new Color("FF9B0F"));
        registry.register(new ItemInfo(Items.POTATO, 0), 0.08f, dirtState, new Color("FFF1B5"));
        registry.register(new ItemInfo(Items.BAKED_POTATO, 0), 0.08f, dirtState, new Color("FFF1B5"));
        registry.register(new ItemInfo(Items.POISONOUS_POTATO, 0), 0.08f, dirtState, new Color("E0FF8A"));

        registry.register(new BlockInfo(Blocks.WATERLILY, 0), 0.10f, dirtState, new Color("269900"));
        registry.register(new BlockInfo(Blocks.VINE, 0), 0.10f, dirtState, new Color("23630E"));
        registry.register(new BlockInfo(Blocks.TALLGRASS, 1), 0.08f, dirtState, new Color("23630E"));
        registry.register(new ItemInfo(Items.EGG, 0), 0.08f, dirtState, new Color("FFFA66"));
        registry.register(new ItemInfo(Items.NETHER_WART, 0), 0.10f, dirtState, new Color("FF2B52"));
        registry.register(new ItemInfo(Items.REEDS, 0), 0.08f, dirtState, new Color("9BFF8A"));
        registry.register(new ItemInfo(Items.STRING, 0), 0.04f, dirtState, Util.whiteColor);

        //Register any missed items
        registry.register("listAllfruit", 0.10f, dirtState, new Color("35A82A"));
        registry.register("listAllveggie", 0.10f, dirtState, new Color("FFF1B5"));
        registry.register("listAllGrain", 0.08f, dirtState, new Color("E3E162"));
        registry.register("listAllseed", 0.08f, dirtState, new Color("35A82A"));
        registry.register("listAllmeatraw", 0.15f, dirtState, new Color("FFA091"));
    }

    public void registerCrook(CrookRegistry registry) {
        registry.register("treeLeaves", ItemResource.getResourceStack(ItemResource.SILKWORM), 0.1f, 0f);
    }

    public void registerSieve(SieveRegistry registry) {

        //Stone Pebble
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 0), getDropChance(1f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 0), getDropChance(1f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 0), getDropChance(0.5f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 0), getDropChance(0.5f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 0), getDropChance(0.1f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 0), getDropChance(0.1f), MeshType.STRING.getID());

        //Granite Pebble
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 1), getDropChance(0.5f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 1), getDropChance(0.1f), MeshType.STRING.getID());

        //Diorite Pebble
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 2), getDropChance(0.5f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 2), getDropChance(0.1f), MeshType.STRING.getID());

        //Andesite Pebble
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 3), getDropChance(0.5f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(ModItems.pebbles, 3), getDropChance(0.1f), MeshType.STRING.getID());

        registry.register("dirt", new ItemInfo(Items.WHEAT_SEEDS, 0), getDropChance(0.7f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(Items.MELON_SEEDS, 0), getDropChance(0.35f), MeshType.STRING.getID());
        registry.register("dirt", new ItemInfo(Items.PUMPKIN_SEEDS, 0), getDropChance(0.35f), MeshType.STRING.getID());

        //Ancient Spores
        registry.register("dirt", new ItemInfo(ModItems.resources, 3), getDropChance(0.05f), MeshType.STRING.getID());
        //Grass Seeds
        registry.register("dirt", new ItemInfo(ModItems.resources, 4), getDropChance(0.05f), MeshType.STRING.getID());


        registry.register("sand", new ItemInfo(Items.DYE, 3), getDropChance(0.03f), MeshType.STRING.getID());
        registry.register("sand", new ItemInfo(Items.PRISMARINE_SHARD, 0), getDropChance(0.02f), MeshType.DIAMOND.getID());

        registry.register("gravel", new ItemInfo(Items.FLINT, 0), getDropChance(0.25f), MeshType.FLINT.getID());
        registry.register("gravel", new ItemInfo(Items.COAL, 0), getDropChance(0.125f), MeshType.FLINT.getID());
        registry.register("gravel", new ItemInfo(Items.DYE, 4), getDropChance(0.05f), MeshType.FLINT.getID());

        registry.register("gravel", new ItemInfo(Items.DIAMOND, 0), getDropChance(0.008f), MeshType.IRON.getID());
        registry.register("gravel", new ItemInfo(Items.EMERALD, 0), getDropChance(0.008f), MeshType.IRON.getID());

        registry.register("gravel", new ItemInfo(Items.DIAMOND, 0), getDropChance(0.016f), MeshType.DIAMOND.getID());
        registry.register("gravel", new ItemInfo(Items.EMERALD, 0), getDropChance(0.016f), MeshType.DIAMOND.getID());


        registry.register(new BlockInfo(Blocks.SOUL_SAND, 0), new ItemInfo(Items.QUARTZ, 0), getDropChance(1f), MeshType.FLINT.getID());
        registry.register(new BlockInfo(Blocks.SOUL_SAND, 0), new ItemInfo(Items.QUARTZ, 0), getDropChance(0.33f), MeshType.FLINT.getID());

        registry.register(new BlockInfo(Blocks.SOUL_SAND, 0), new ItemInfo(Items.NETHER_WART, 0), getDropChance(0.1f), MeshType.STRING.getID());

        registry.register(new BlockInfo(Blocks.SOUL_SAND, 0), new ItemInfo(Items.GHAST_TEAR, 0), getDropChance(0.02f), MeshType.DIAMOND.getID());
        registry.register(new BlockInfo(Blocks.SOUL_SAND, 0), new ItemInfo(Items.QUARTZ, 0), getDropChance(1f), MeshType.DIAMOND.getID());
        registry.register(new BlockInfo(Blocks.SOUL_SAND, 0), new ItemInfo(Items.QUARTZ, 0), getDropChance(0.8f), MeshType.DIAMOND.getID());

        registry.register(new BlockInfo(ModBlocks.dust, 0), new ItemInfo(Items.DYE, 15), getDropChance(0.2f), MeshType.STRING.getID());
        registry.register(new BlockInfo(ModBlocks.dust, 0), new ItemInfo(Items.GUNPOWDER, 0), getDropChance(0.07f), MeshType.STRING.getID());

        registry.register(new BlockInfo(ModBlocks.dust, 0), new ItemInfo(Items.REDSTONE, 0), getDropChance(0.125f), MeshType.IRON.getID());
        registry.register(new BlockInfo(ModBlocks.dust, 0), new ItemInfo(Items.REDSTONE, 0), getDropChance(0.25f), MeshType.DIAMOND.getID());

        registry.register(new BlockInfo(ModBlocks.dust, 0), new ItemInfo(Items.GLOWSTONE_DUST, 0), getDropChance(0.0625f), MeshType.IRON.getID());
        registry.register(new BlockInfo(ModBlocks.dust, 0), new ItemInfo(Items.BLAZE_POWDER, 0), getDropChance(0.05f), MeshType.IRON.getID());

        // Custom Ores for other mods
        OreRegistry oreRegistry = ExNihiloRegistryManager.ORE_REGISTRY;

        // Gold from nether rack
        ItemOre gold = oreRegistry.getOreItem("gold");
        if (gold != null) {
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed, 0), new ItemInfo(gold, 0), getDropChance(0.25f), MeshType.FLINT.getID());
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed, 0), new ItemInfo(gold, 0), getDropChance(0.25f), MeshType.IRON.getID());
            registry.register(new BlockInfo(ModBlocks.netherrackCrushed, 0), new ItemInfo(gold, 0), getDropChance(0.4f), MeshType.DIAMOND.getID());
        }


        // All default Ores
        for (ItemOre ore : oreRegistry.getItemOreRegistry()) {
            if (oreRegistry.getSieveBlackList().contains(ore)) continue;
            registry.register("gravel", new ItemInfo(ore, 0), getDropChance(0.2f), MeshType.FLINT.getID());
            registry.register("gravel", new ItemInfo(ore, 0), getDropChance(0.2f), MeshType.IRON.getID());
            registry.register("gravel", new ItemInfo(ore, 0), getDropChance(0.1f), MeshType.DIAMOND.getID());

        }
        // Seeds
        for (ItemSeedBase seed : ModItems.itemSeeds) {
            registry.register("dirt", new ItemInfo(seed), getDropChance(0.05f), MeshType.STRING.getID());
        }

    }

    public void registerHammer(HammerRegistry registry) {
        registry.register("cobblestone", new ItemStack(Blocks.GRAVEL, 1), 0, 1.0F, 0.0F);
        registry.register("gravel", new ItemStack(Blocks.SAND, 1), 0, 1.0F, 0.0F);
        registry.register("sand", new ItemStack(ModBlocks.dust, 1), 0, 1.0F, 0.0F);
        registry.register("netherrack", new ItemStack(ModBlocks.netherrackCrushed, 1), 0, 1.0F, 0.0F);
        registry.register("endstone", new ItemStack(ModBlocks.endstoneCrushed, 1), 0, 1.0F, 0.0F);

        // Hammering stone into pebbles (no idea why anyone should do that, but hey :P)
        registry.register(new BlockInfo(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)), new ItemStack(ModItems.pebbles, 1, EnumPebbleSubtype.STONE.getMeta()), 1, 3F, 1.25F);
        registry.register(new BlockInfo(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE)), new ItemStack(ModItems.pebbles, 1, EnumPebbleSubtype.ANDESITE.getMeta()), 1, 3F, 1.25F);
        registry.register(new BlockInfo(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE)), new ItemStack(ModItems.pebbles, 1, EnumPebbleSubtype.GRANITE.getMeta()), 1, 3F, 1.25F);
        registry.register(new BlockInfo(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE)), new ItemStack(ModItems.pebbles, 1, EnumPebbleSubtype.DIORITE.getMeta()), 1, 3F, 1.25F);

        // Fill in the rest
        registry.register("stone", new ItemStack(ModItems.pebbles, 1, EnumPebbleSubtype.STONE.getMeta()), 1, 3F, 1.25F);

        // Hammer concrete into concrete powder
        for (int meta = 0; meta < 16; meta++)
            registry.register(new BlockInfo(Blocks.CONCRETE.getStateFromMeta(meta)), new ItemStack(Blocks.CONCRETE_POWDER, 1, meta), 1, 1.0f, 0.0f);

    }

    public void registerHeat(HeatRegistry registry) {
        // Vanilla fluids are weird, the "flowing" variant is simply a temporary state of checking if it can flow.
        // So, once the lava has spread out all the way, it will all actually be "still" lava.
        // Thanks Mojang <3
        registry.register(new BlockInfo(Blocks.FLOWING_LAVA, -1), 3);
        registry.register(new BlockInfo(Blocks.LAVA, -1), 3);
        registry.register(new BlockInfo(Blocks.FIRE, -1), 4);
        registry.register(new BlockInfo(Blocks.TORCH, -1), 1);
    }

    public void registerBarrelLiquidBlacklist(BarrelLiquidBlacklistRegistry registry) {
        registry.register(ModBlocks.barrelWood.getTier(), "lava");
        registry.register(ModBlocks.barrelWood.getTier(), "fire_water");
        registry.register(ModBlocks.barrelWood.getTier(), "rocket_fuel");
    }

    public void registerFluidOnTop(FluidOnTopRegistry registry) {
        registry.register(FluidRegistry.LAVA, FluidRegistry.WATER, new ItemInfo(Blocks.OBSIDIAN.getDefaultState()));
        registry.register(FluidRegistry.WATER, FluidRegistry.LAVA, new ItemInfo(Blocks.COBBLESTONE.getDefaultState()));
    }

    public void registerOreChunks(OreRegistry registry) {
        registry.register("gold", new Color("FFFF00"), new ItemInfo(Items.GOLD_INGOT, 0));
        registry.register("iron", new Color("BF8040"), new ItemInfo(Items.IRON_INGOT, 0));

        //TODO: Better way, will most likely never grab as it is just called before many mods init their oredict
        if (!OreDictionary.getOres("oreCopper").isEmpty()) {
            registry.register("copper", new Color("FF9933"), null);
        }

        if (!OreDictionary.getOres("oreTin").isEmpty()) {
            registry.register("tin", new Color("E6FFF2"), null);
        }

        if (!OreDictionary.getOres("oreAluminium").isEmpty() || !OreDictionary.getOres("oreAluminum").isEmpty()) {
            registry.register("aluminium", new Color("BFBFBF"), null);
        }

        if (!OreDictionary.getOres("oreLead").isEmpty()) {
            registry.register("lead", new Color("330066"), null);
        }

        if (!OreDictionary.getOres("oreSilver").isEmpty()) {
            registry.register("silver", new Color("F2F2F2"), null);
        }

        if (!OreDictionary.getOres("oreNickel").isEmpty()) {
            registry.register("nickel", new Color("FFFFCC"), null);
        }
    }

    public void registerFluidTransform(FluidTransformRegistry registry) {
        registry.register("water", "witchwater", 12000, new BlockInfo[]{new BlockInfo(Blocks.MYCELIUM.getDefaultState())}, new BlockInfo[]{new BlockInfo(Blocks.BROWN_MUSHROOM.getDefaultState()), new BlockInfo(Blocks.RED_MUSHROOM.getDefaultState())});
    }

    public void registerFluidBlockTransform(FluidBlockTransformerRegistry registry) {
        registry.register(FluidRegistry.WATER, new ItemInfo(new ItemStack(ModBlocks.dust)), new ItemInfo(new ItemStack(Blocks.CLAY)));
        registry.register(FluidRegistry.LAVA, new ItemInfo(new ItemStack(Items.REDSTONE)), new ItemInfo(new ItemStack(Blocks.NETHERRACK)));
        registry.register(FluidRegistry.LAVA, new ItemInfo(new ItemStack(Items.GLOWSTONE_DUST)), new ItemInfo(new ItemStack(Blocks.END_STONE)));
        registry.register(ModFluids.fluidWitchwater, new ItemInfo(new ItemStack(Blocks.SAND)), new ItemInfo(new ItemStack(Blocks.SOUL_SAND)));

        // Vanilla Concrete
        for (int meta = 0; meta < 16; meta++)
            registry.register(FluidRegistry.WATER, new ItemInfo(new ItemStack(Blocks.CONCRETE_POWDER, 1, meta)), new ItemInfo(new ItemStack(Blocks.CONCRETE, 1, meta)));
    }

    public void registerFluidItemFluid(FluidItemFluidRegistry registry) {
        registry.register(FluidRegistry.WATER, new ItemInfo(ItemResource.getResourceStack(ItemResource.ANCIENT_SPORES)), ModFluids.fluidWitchwater);
    }

    public void registerCrucibleStone(CrucibleRegistry registry) {
        registry.register("cobblestone", FluidRegistry.LAVA, 250);
        registry.register("stone", FluidRegistry.LAVA, 250);
        registry.register("gravel", FluidRegistry.LAVA, 200);
        registry.register("sand", FluidRegistry.LAVA, 100);
        registry.register(new BlockInfo(ModBlocks.dust), FluidRegistry.LAVA, 50);

        // 1:1 Back to lava
        registry.register("netherrack", FluidRegistry.LAVA, 1000);
        registry.register("obsidian", FluidRegistry.LAVA, 1000);
    }

    public void registerCrucibleWood(CrucibleRegistry registry) {
        Meltable water = new Meltable(FluidRegistry.WATER.getName(), 250, new BlockInfo(Blocks.LEAVES, 0));
        registry.register("treeLeaves", FluidRegistry.WATER, 250);
        registry.register("treeSapling", water);
        registry.register("listAllfruit", water);
        registry.register("listAllveggie", water);

        registry.register(new ItemInfo(Blocks.SAPLING, 0), water);
        registry.register(new ItemInfo(Blocks.SAPLING, 1), water.copy().setTextureOverride(new BlockInfo(Blocks.LEAVES, 1)));
        registry.register(new ItemInfo(Blocks.SAPLING, 2), water.copy().setTextureOverride(new BlockInfo(Blocks.LEAVES, 2)));
        registry.register(new ItemInfo(Blocks.SAPLING, 3), water.copy().setTextureOverride(new BlockInfo(Blocks.LEAVES, 3)));
        registry.register(new ItemInfo(Blocks.SAPLING, 4), water.copy().setTextureOverride(new BlockInfo(Blocks.LEAVES2, 0)));
        registry.register(new ItemInfo(Blocks.SAPLING, 5), water.copy().setTextureOverride(new BlockInfo(Blocks.LEAVES2, 1)));
        registry.register(new ItemInfo(Items.APPLE, 0), water);
    }

    public void registerMilk(MilkEntityRegistry registry) {
        registry.register("Cow", "milk", 10, 20);
    }

    private float getDropChance(float chance) {
        if (ModConfig.world.isSkyWorld)
            return chance;
        else return chance / 100f * (float) ModConfig.world.normalDropPercent;
    }
}
