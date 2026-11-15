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
        // 1.21.11 turned FallingBlock#getColor into an abstract method, so it
        // has to be implemented here. The only caller in the game is the
        // falling-dust particle factory, which lets this value override the
        // colour BlockColors would otherwise derive from the block's texture.
        // Returning a constant 0 made every crushed block's dust pure black;
        // the map colour is what the original 1.21 branch produced, because
        // BlockColors#getParticleColor falls back to it when no colour
        // provider is registered for the block.
        return state.getMapColor(world, pos).color;
    }
}
