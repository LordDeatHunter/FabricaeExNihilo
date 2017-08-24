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

    public static final HammerBase hammerWood;
    public static final HammerBase hammerStone;
    public static final HammerBase hammerIron;
    public static final HammerBase hammerDiamond;
    public static final HammerBase hammerGold;

    public static final CrookBase crookWood;
    public static final CrookBase crookBone;

    public static final ItemMesh mesh;

    public static final ItemResource resources;
    public static final ItemCookedSilkworm cookedSilkworm;

    public static final ItemPebble pebbles;

    public static final ItemDoll dolls;

    public static final ArrayList<ItemSeedBase> itemSeeds = new ArrayList<>();

    static {
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

        for (Block block : Data.BLOCKS) {
            if (!(block instanceof IHasSpecialRegistry)) {
                registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }

        registry.register(new ItemBlockCrucible(ModBlocks.crucibleStone));

        OreDictionary.registerOre("clayPorcelain", ItemResource.getResourceStack(ItemResource.PORCELAIN_CLAY));
        OreDictionary.registerOre("gearStone", ItemResource.getResourceStack(ItemResource.GEAR_STONE));
        OreDictionary.registerOre("stickStone", ItemResource.getResourceStack(ItemResource.ROD_STONE));
        OreDictionary.registerOre("rodStone", ItemResource.getResourceStack(ItemResource.ROD_STONE));

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
