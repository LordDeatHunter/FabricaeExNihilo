package exnihilocreatio.modules.forestry.items;

import exnihilocreatio.modules.forestry.blocks.BlockHive;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockHive extends ItemBlock {
    public ItemBlockHive(Block block){
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    @Override
    public int getMetadata(int metadata){
        return metadata;
    }
    @Override
    public String getTranslationKey(ItemStack stack){
        BlockHive.EnumType type = BlockHive.EnumType.values()[stack.getMetadata()];
        return super.getTranslationKey(stack) + "." + type.toString();
    }
}
