package wraith.fabricaeexnihilo.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class DummyFallingBlock extends FallingBlock {
    private final MapCodec<? extends FallingBlock> CODEC = createCodec(DummyFallingBlock::new);

    public DummyFallingBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return 0;
    }
}
