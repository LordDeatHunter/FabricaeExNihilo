package exnihiloadscensio.blocks;

import exnihiloadscensio.items.ItemBlockMeta;
import net.minecraft.block.Block;

public class ItemBlockCrucible extends ItemBlockMeta {
    public ItemBlockCrucible(Block block) {
        super(block);
        
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return super.getMetadata(damage);
    }
}
