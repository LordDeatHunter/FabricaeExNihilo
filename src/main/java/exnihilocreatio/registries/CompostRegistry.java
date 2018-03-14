package exnihilocreatio.registries;

import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.types.Compostable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import exnihilocreatio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

@Deprecated
public class CompostRegistry {

    public static void register(ItemInfo item, Compostable compostable) {
        ExNihiloRegistryManager.COMPOST_REGISTRY.register(CraftingHelper.getIngredient(item.getItemStack()), compostable);
    }

    public static void register(Item item, int meta, float value, IBlockState state, Color color) {
        ExNihiloRegistryManager.COMPOST_REGISTRY.register(item, meta, value, new BlockInfo(state), color);
    }

    public static void register(Block block, int meta, float value, IBlockState state, Color color) {
        ExNihiloRegistryManager.COMPOST_REGISTRY.register(block, meta, value, new BlockInfo(state), color);
    }

    public static Compostable getItem(Item item, int meta) {
        return ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(item, meta);
    }

    public static Compostable getItem(ItemStack stack) {
        return ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(stack);
    }

    public static Compostable getItem(ItemInfo info) {
        return ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(info);
    }

    public static boolean containsItem(Item item, int meta) {
        return ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(item, meta);
    }

    public static boolean containsItem(ItemStack stack) {
        return ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(stack);
    }

    public static boolean containsItem(ItemInfo info) {
        return ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(info);
    }
}
