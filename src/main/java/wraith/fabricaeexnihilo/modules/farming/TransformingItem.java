package wraith.fabricaeexnihilo.modules.farming;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import wraith.fabricaeexnihilo.util.Lazy;

public class TransformingItem extends Item {
    
    private final Lazy<Block> from;
    private final Lazy<BlockState> to;
    
    public TransformingItem(Lazy<Block> from, Lazy<BlockState> to, FabricItemSettings settings) {
        super(settings);
        this.from = from;
        this.to = to;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var blockPos = context.getBlockPos();
        var target = world.getBlockState(blockPos).getBlock();
        // TODO make this work if someone overrides a block
        if (target == from.get()) {
            world.setBlockState(blockPos, to.get());
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
    
}