package wraith.fabricaeexnihilo.modules;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlock;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockItem;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlock;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestedLeavesItem;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlock;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlock;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;
import wraith.fabricaeexnihilo.util.BlockGenerator;
import wraith.fabricaeexnihilo.util.VanillaWoodDefinitions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ModBlocks {

    public static final FabricBlockSettings WOOD_SETTINGS = FabricBlockSettings.of(Material.WOOD).strength(2.0f).sounds(BlockSoundGroup.WOOD).breakByHand(true);
    public static final FabricBlockSettings STONE_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(2.0f, 6.0f).sounds(BlockSoundGroup.STONE);
    public static final FabricBlockSettings CRUSHED_GRAVELY_SETTINGS = FabricBlockSettings.of(Material.AGGREGATE).strength(0.6f).sounds(BlockSoundGroup.GRAVEL).breakByHand(true);
    public static final FabricBlockSettings CRUSHED_SANDY_SETTINGS = FabricBlockSettings.of(Material.AGGREGATE).strength(0.4f).sounds(BlockSoundGroup.SAND).breakByHand(true);
    public static final FabricBlockSettings INFESTED_LEAVES_SETTINGS = FabricBlockSettings.of(Material.LEAVES).nonOpaque().suffocates((state, world, pos) -> false).allowsSpawning((state, world, pos, type) -> type == EntityType.OCELOT || type == EntityType.PARROT).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS);

    public static final Map<Identifier, SieveBlock> SIEVES = new HashMap<>();
    public static final Map<Identifier, CrucibleBlock> CRUCIBLES = new HashMap<>();
    public static final Map<Identifier, BarrelBlock> BARRELS = new HashMap<>();

    public static final Map<Identifier, Block> CRUSHED = new HashMap<>();

    public static final InfestingLeavesBlock INFESTING_LEAVES = new InfestingLeavesBlock(INFESTED_LEAVES_SETTINGS);
    public static final Map<Identifier, InfestedLeavesBlock> INFESTED_LEAVES = new HashMap<>();

    public static void registerBlocks() {
        SIEVES.forEach((identifier, block) -> Registry.register(Registry.BLOCK, identifier, block));
        CRUCIBLES.forEach((identifier, block) -> Registry.register(Registry.BLOCK, identifier, block));
        BARRELS.forEach((identifier, block) -> Registry.register(Registry.BLOCK, identifier, block));
        CRUSHED.forEach((identifier, block) -> Registry.register(Registry.BLOCK, identifier, block));
        INFESTED_LEAVES.forEach((identifier, block) -> Registry.register(Registry.BLOCK, identifier, block));

        Registry.register(Registry.BLOCK, FabricaeExNihilo.id("infesting_leaves"), INFESTING_LEAVES);

        ModFluids.registerFluidBlocks();
    }

    public static void registerBlockItems() {
        SIEVES.forEach((identifier, block) -> Registry.register(Registry.ITEM, identifier, new BlockItem(block, ModItems.BASE_SETTINGS)));
        CRUCIBLES.forEach((identifier, block) -> {
            var ench = block.getDefaultState().getMaterial() == Material.STONE
                    ? ToolMaterials.STONE.getEnchantability()
                    : ToolMaterials.WOOD.getEnchantability();
            Registry.register(Registry.ITEM, identifier, new EnchantableBlockItem(block, ModItems.BASE_SETTINGS, ench));
        });
        BARRELS.forEach((identifier, block) -> {
            var ench = block.getDefaultState().getMaterial() == Material.STONE
                    ? ToolMaterials.STONE.getEnchantability()
                    : ToolMaterials.WOOD.getEnchantability();
            Registry.register(Registry.ITEM, identifier, new EnchantableBlockItem(block, ModItems.BASE_SETTINGS, ench));
        });
        CRUSHED.forEach((identifier, block) -> Registry.register(Registry.ITEM, identifier, new BlockItem(block, ModItems.BASE_SETTINGS)));
        INFESTED_LEAVES.forEach((identifier, block) -> Registry.register(Registry.ITEM, identifier, new InfestedLeavesItem(block, ModItems.BASE_SETTINGS)));
        BlockGenerator.INSTANCE.initRegistryCallBack();
    }

    public static void registerBlockEntities() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, SieveBlockEntity.BLOCK_ENTITY_ID, SieveBlockEntity.TYPE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, CrucibleBlockEntity.BLOCK_ENTITY_ID, CrucibleBlockEntity.TYPE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BarrelBlockEntity.BLOCK_ENTITY_ID, BarrelBlockEntity.TYPE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, InfestingLeavesBlockEntity.BLOCK_ENTITY_ID, InfestingLeavesBlockEntity.TYPE);
    }

    public static void generateDerivedBlocks() {
        Arrays.stream(VanillaWoodDefinitions.values()).forEach(wood -> {
            var planksID = wood.getPlanksID();
            var slabID = wood.getSlabID();
            var logID = wood.getLogID();
            var leafBlock = wood.getLeafBlock();

            BlockGenerator.INSTANCE.createInfestedLeavesBlock((LeavesBlock) leafBlock);
            BlockGenerator.INSTANCE.createSieveBlock(planksID, slabID);
            BlockGenerator.INSTANCE.createWoodBarrelBlock(planksID, slabID);
            BlockGenerator.INSTANCE.createWoodCrucibleBlock(logID);
        });
    }

    static {
        CRUSHED.put(FabricaeExNihilo.id("dust"), new FallingBlock(CRUSHED_SANDY_SETTINGS));
        CRUSHED.put(FabricaeExNihilo.id("silt"), new FallingBlock(CRUSHED_SANDY_SETTINGS));
        CRUSHED.put(FabricaeExNihilo.id("crushed_andesite"), new FallingBlock(CRUSHED_GRAVELY_SETTINGS));
        CRUSHED.put(FabricaeExNihilo.id("crushed_diorite"), new FallingBlock(CRUSHED_GRAVELY_SETTINGS));
        CRUSHED.put(FabricaeExNihilo.id("crushed_granite"), new FallingBlock(CRUSHED_GRAVELY_SETTINGS));
        CRUSHED.put(FabricaeExNihilo.id("crushed_prismarine"), new FallingBlock(CRUSHED_GRAVELY_SETTINGS));
        CRUSHED.put(FabricaeExNihilo.id("crushed_endstone"), new FallingBlock(CRUSHED_GRAVELY_SETTINGS));
        CRUSHED.put(FabricaeExNihilo.id("crushed_netherrack"), new FallingBlock(CRUSHED_GRAVELY_SETTINGS));
        //CRUSHED.put(FabricaeExNihilo.id("crushed_skystone"), new FallingBlock(CRUSHED_GRAVELY_SETTINGS));

        CRUCIBLES.put(FabricaeExNihilo.id("stone_crucible"), new CrucibleBlock(FabricaeExNihilo.id("block/stone_crucible"), FabricaeExNihilo.id("porcelain"), STONE_SETTINGS));

        BARRELS.put(FabricaeExNihilo.id("stone_barrel"), new BarrelBlock(new Identifier("block/stone"), new Identifier("stone"), new Identifier("stone_slab"), STONE_SETTINGS));
    }

}
