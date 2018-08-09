package exnihilocreatio;

import exnihilocreatio.blocks.ItemBlockCrucible;
import exnihilocreatio.config.ModConfig;
import exnihilocreatio.items.*;
import exnihilocreatio.items.ore.EnumOreSubtype;
import exnihilocreatio.items.ore.ItemOre;
import exnihilocreatio.items.seeds.ItemSeedBase;
import exnihilocreatio.items.tools.CrookBase;
import exnihilocreatio.items.tools.HammerBase;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import exnihilocreatio.util.IHasSpecialRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ModItems {

    public static final HammerBase hammerWood = new HammerBase("hammer_wood", 64, Item.ToolMaterial.WOOD);
    public static final HammerBase hammerStone = new HammerBase("hammer_stone", 128, Item.ToolMaterial.STONE);
    public static final HammerBase hammerIron = new HammerBase("hammer_iron", 512, Item.ToolMaterial.IRON);
    public static final HammerBase hammerDiamond = new HammerBase("hammer_diamond", 4096, Item.ToolMaterial.DIAMOND);
    public static final HammerBase hammerGold = new HammerBase("hammer_gold", 64, Item.ToolMaterial.GOLD);

    public static final CrookBase crookWood = new CrookBase("crook_wood", 64);
    public static final CrookBase crookBone = new CrookBase("crook_bone", 256);

    public static final ItemMesh mesh = new ItemMesh();

    public static final ItemResource resources = new ItemResource();
    public static final ItemCookedSilkworm cookedSilkworm = new ItemCookedSilkworm();

    public static final ItemPebble pebbles = new ItemPebble();

    public static final ItemDoll dolls = new ItemDoll();

    public static final ArrayList<ItemSeedBase> itemSeeds = new ArrayList<>(Arrays.asList(
            new ItemSeedBase("oak", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK)),
            new ItemSeedBase("spruce", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.SPRUCE)),
            new ItemSeedBase("birch", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.BIRCH)),
            new ItemSeedBase("jungle", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE)),
            new ItemSeedBase("acacia", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.ACACIA)),
            new ItemSeedBase("darkoak", Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.DARK_OAK)),
            new ItemSeedBase("cactus", Blocks.CACTUS.getDefaultState()).setPlantType(EnumPlantType.Desert),
            new ItemSeedBase("sugarcane", Blocks.REEDS.getDefaultState()).setPlantType(EnumPlantType.Beach),
            new ItemSeedBase("carrot", Blocks.CARROTS.getDefaultState()).setPlantType(EnumPlantType.Crop),
            new ItemSeedBase("potato", Blocks.POTATOES.getDefaultState()).setPlantType(EnumPlantType.Crop)
    ));

    public static void registerItems(IForgeRegistry<Item> registry) {
        for (Item item : Data.ITEMS) {
            if (!(item instanceof IHasSpecialRegistry)) {
                registry.register(item);
            }
        }

        for (Block block : Data.BLOCKS) {
            if (!(block instanceof IHasSpecialRegistry)) {
                registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }

        registry.register(new ItemBlockCrucible(ModBlocks.crucibleStone));
    }

    public static void registerItemsLowest(IForgeRegistry<Item> registry) {
        ExNihiloRegistryManager.ORE_REGISTRY.loadJson(new File(ExNihiloCreatio.configDirectory, "OreRegistry.json"));
        ExNihiloRegistryManager.ORE_REGISTRY.registerToGameRegistry(registry);
        ExNihiloRegistryManager.ORE_REGISTRY.doRecipes();

        registerOredicts();
    }

    public static void registerOredicts(){
        OreDictionary.registerOre("clayPorcelain", ItemResource.getResourceStack(ItemResource.PORCELAIN_CLAY));
        OreDictionary.registerOre("gearStone", ItemResource.getResourceStack(ItemResource.GEAR_STONE));
        OreDictionary.registerOre("stickStone", ItemResource.getResourceStack(ItemResource.ROD_STONE));
        OreDictionary.registerOre("rodStone", ItemResource.getResourceStack(ItemResource.ROD_STONE));
        OreDictionary.registerOre("dust", ModBlocks.dust);
        OreDictionary.registerOre("crushedGranite", ModBlocks.crushedGranite);
        OreDictionary.registerOre("crushedAndesite", ModBlocks.crushedAndesite);
        OreDictionary.registerOre("crushedDiorite", ModBlocks.crushedDiorite);


        if (ModConfig.misc.oredictVanillaItems){
            // Flowers:
            OreDictionary.registerOre("flower", new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE));
            OreDictionary.registerOre("flower", new ItemStack(Blocks.DOUBLE_PLANT, 1, OreDictionary.WILDCARD_VALUE));
            OreDictionary.registerOre("flower", new ItemStack(Blocks.YELLOW_FLOWER, 1, OreDictionary.WILDCARD_VALUE));

            // Meat:
            OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_BEEF);
            OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_CHICKEN);
            OreDictionary.registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_FISH, 1, OreDictionary.WILDCARD_VALUE));
            OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_PORKCHOP);
            OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_MUTTON);
            OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_RABBIT);
        }


        if (ModConfig.misc.oredictExNihiloSeeds){
            // Seeds
            for(ItemSeedBase seed : itemSeeds){
                OreDictionary.registerOre("listAllseed", (Item) seed);
            }
            // Grass Seeds
            OreDictionary.registerOre("listAllseed", new ItemStack((Item) resources, 1, 4));
        }

        ItemOre oreYellorium = ExNihiloRegistryManager.ORE_REGISTRY.getOreItem("yellorium");
        if (ModConfig.compatibility.addYelloriteOreDict && oreYellorium != null){
            OreDictionary.registerOre("oreYellorite", new ItemStack(oreYellorium, 1, EnumOreSubtype.CHUNK.getMeta()));
            OreDictionary.registerOre("oreUranium", new ItemStack(oreYellorium, 1, EnumOreSubtype.CHUNK.getMeta()));
            OreDictionary.registerOre("dustUranium", new ItemStack(oreYellorium, 1, EnumOreSubtype.DUST.getMeta()));
            OreDictionary.registerOre("pieceUranium", new ItemStack(oreYellorium, 1, EnumOreSubtype.PIECE.getMeta()));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(ModelRegistryEvent e) {
        for (Item item : Data.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).initModel(e);
            }
        }
    }
}
