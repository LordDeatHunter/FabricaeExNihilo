package exnihilocreatio.items;

import exnihilocreatio.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockMeta extends ItemBlock implements IHasModel {

    public ItemBlockMeta(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setRegistryName(block.getRegistryName());
    }

    public int getMetadata(int damage) {
        return damage;
    }


    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + "." + stack.getItemDamage();
    }
}
