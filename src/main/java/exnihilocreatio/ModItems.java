package exnihilocreatio;

import exnihilocreatio.blocks.ItemBlockCrucible;
import exnihilocreatio.items.*;
import exnihilocreatio.items.seeds.ItemSeedBase;
import exnihilocreatio.items.tools.CrookBase;
import exnihilocreatio.items.tools.HammerBase;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import exnihilocreatio.util.IHasSpecialRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class ModItems {

    public static HammerBase hammerWood;
    public static HammerBase hammerStone;
    public static HammerBase hammerIron;
    public static HammerBase hammerDiamond;
    public static HammerBase hammerGold;

    public static CrookBase crookWood;
    public static CrookBase crookBone;

    public static ItemMesh mesh;

    public static ItemResource resources;
    public static ItemCookedSilkworm cookedSilkworm;

    public static ItemPebble pebbles;

    public static ItemDoll dolls;

    public static ArrayList<ItemSeedBase> itemSeeds = new ArrayList<>();

    @SuppressWarnings("deprecation")
    public static void preInit() {
        hammerWood = new HammerBase("hammer_wood", 64, Item.ToolMaterial.WOOD);
        hammerWood.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        hammerStone = new HammerBase("hammer_stone", 128, Item.ToolMaterial.STONE);
        hammerStone.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        hammerIron = new HammerBase("hammer_iron", 512, Item.ToolMaterial.IRON);
        hammerIron.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        hammerDiamond = new HammerBase("hammer_diamond", 4096, Item.ToolMaterial.DIAMOND);
        hammerDiamond.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        hammerGold = new HammerBase("hammer_gold", 64, Item.ToolMaterial.GOLD);
        hammerGold.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        crookWood = new CrookBase("crook_wood", 64);
        crookWood.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        crookBone = new CrookBase("crook_bone", 256);
        crookBone.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        mesh = new ItemMesh();
        mesh.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        resources = new ItemResource();

        cookedSilkworm = new ItemCookedSilkworm();

        pebbles = new ItemPebble();

        itemSeeds.add(new ItemSeedBase("oak", Blocks.SAPLING.getStateFromMeta(0)));
        itemSeeds.add(new ItemSeedBase("spruce", Blocks.SAPLING.getStateFromMeta(1)));
        itemSeeds.add(new ItemSeedBase("birch", Blocks.SAPLING.getStateFromMeta(2)));
        itemSeeds.add(new ItemSeedBase("jungle", Blocks.SAPLING.getStateFromMeta(3)));
        itemSeeds.add(new ItemSeedBase("acacia", Blocks.SAPLING.getStateFromMeta(4)));
        itemSeeds.add(new ItemSeedBase("darkoak", Blocks.SAPLING.getStateFromMeta(5)));
        itemSeeds.add(new ItemSeedBase("cactus", Blocks.CACTUS.getDefaultState()).setPlantType(EnumPlantType.Desert));
        itemSeeds.add(new ItemSeedBase("sugarcane", Blocks.REEDS.getDefaultState()).setPlantType(EnumPlantType.Beach));
        itemSeeds.add(new ItemSeedBase("carrot", Blocks.CARROTS.getDefaultState()).setPlantType(EnumPlantType.Crop));
        itemSeeds.add(new ItemSeedBase("potato", Blocks.POTATOES.getDefaultState()).setPlantType(EnumPlantType.Crop));

        dolls = new ItemDoll();
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        for (Item item : Data.ITEMS) {
            if (!(item instanceof IHasSpecialRegistry)) {
                registry.register(item);
            }
        }

        for (Block block : Data.BLOCKS)
            if (!(block instanceof IHasSpecialRegistry)) {
                registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }

        registry.register(new ItemBlockCrucible(ModBlocks.crucible));

        OreDictionary.registerOre("clayPorcelain", ItemResource.getResourceStack("porcelain_clay"));

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
