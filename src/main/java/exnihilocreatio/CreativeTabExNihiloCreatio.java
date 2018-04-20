package exnihilocreatio;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class CreativeTabExNihiloCreatio extends CreativeTabs {

    CreativeTabExNihiloCreatio() {
        super("exNihilo");
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.sieve), 1);
    }
}
