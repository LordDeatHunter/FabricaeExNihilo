package wraith.fabricaeexnihilo.modules.infested;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.mixins.BlockWithEntityInvoker;

public class InfestingLeavesBlock extends LeavesBlock implements BlockEntityProvider, NonInfestableLeavesBlock {
    
    private final InfestedLeavesBlock target;
    
    public InfestingLeavesBlock(FabricBlockSettings settings, InfestedLeavesBlock target) {
        super(settings);
        this.target = target;
    }
    
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfestingLeavesBlockEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : BlockWithEntityInvoker.checkType(type, InfestingLeavesBlockEntity.TYPE, InfestingLeavesBlockEntity::ticker);
    }
    
    public InfestedLeavesBlock getTarget() {
        return target;
    }
}