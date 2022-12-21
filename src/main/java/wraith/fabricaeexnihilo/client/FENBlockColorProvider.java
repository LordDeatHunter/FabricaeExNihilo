package wraith.fabricaeexnihilo.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public final class FENBlockColorProvider implements BlockColorProvider {

    private FENBlockColorProvider() {
    }

    public static final FENBlockColorProvider INSTANCE = new FENBlockColorProvider();

    @Override
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        return state.getBlock() instanceof Colored colored ? colored.getColor(tintIndex) : Color.WHITE.toInt();
    }

}