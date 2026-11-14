package wraith.fabricaeexnihilo.modules.infested;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.mixins.BlockWithEntityInvoker;

public class InfestingLeavesBlock extends LeavesBlock implements BlockEntityProvider, NonInfestableLeavesBlock {

    public static final MapCodec<InfestingLeavesBlock> CODEC = createCodec(InfestingLeavesBlock::new);

    public InfestingLeavesBlock(AbstractBlock.Settings settings) {
        super(0.01f, settings);
    }

    @Override
    public MapCodec<InfestingLeavesBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void spawnLeafParticle(World world, BlockPos pos, Random random) {
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfestingLeavesBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockWithEntityInvoker.checkType(type, InfestingLeavesBlockEntity.TYPE, InfestingLeavesBlockEntity::ticker);
    }
}
