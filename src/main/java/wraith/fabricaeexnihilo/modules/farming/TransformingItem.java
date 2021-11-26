package wraith.fabricaeexnihilo.modules.farming;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class TransformingItem extends Item {

    private final Block fromBlock;
    private final BlockState toBlockState;

    public TransformingItem(Block fromBlock, Block toBlock, FabricItemSettings settings) {
        this(fromBlock, toBlock.getDefaultState(), settings);
    }

    public TransformingItem(Block fromBlock, BlockState toBlockState, FabricItemSettings settings) {
        super(settings);
        this.fromBlock = fromBlock;
        this.toBlockState = toBlockState;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        if(world.isClient) {
            return ActionResult.SUCCESS;
        }
        var blockPos = context.getBlockPos();
        var target = world.getBlockState(blockPos).getBlock();
        // TODO make this work if someone overrides a block
        if(target == fromBlock) {
            world.setBlockState(blockPos, toBlockState);
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

}