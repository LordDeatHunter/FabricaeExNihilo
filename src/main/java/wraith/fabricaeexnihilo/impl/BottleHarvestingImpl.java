package wraith.fabricaeexnihilo.impl;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.util.ItemUtils;

public final class BottleHarvestingImpl {

    public static ItemStack getResult(BlockState target) {
        return target.getBlock() != Blocks.SAND ? ItemStack.EMPTY : new ItemStack(ItemUtils.getExNihiloItem("salt_bottle"));
    }

}
