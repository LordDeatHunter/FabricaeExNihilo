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
            new ItemSeedBase("oak", Blocks.SAPLING.getStateFromMeta(0)),
            new ItemSeedBase("spruce", Blocks.SAPLING.getStateFromMeta(1)),
            new ItemSeedBase("birch", Blocks.SAPLING.getStateFromMeta(2)),
            new ItemSeedBase("jungle", Blocks.SAPLING.getStateFromMeta(3)),
            new ItemSeedBase("acacia", Blocks.SAPLING.getStateFromMeta(4)),
            new ItemSeedBase("darkoak", Blocks.SAPLING.getStateFromMeta(5)),
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
