package exnihilocreatio;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabExNihiloCreatio extends CreativeTabs {

    public CreativeTabExNihiloCreatio() {
        super("exNihilo");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.sieve), 1);
    }
}
